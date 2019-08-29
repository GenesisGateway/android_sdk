package com.emerchantpay.gateway.genesisandroid.api.util

import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.StringReader
import java.util.*
import java.util.regex.Pattern
import javax.xml.parsers.SAXParserFactory

class SimpleNodeWrapper private constructor(override val elementName: String) : NodeWrapper() {
    private val attributes = HashMap<String, String>()
    private val content = LinkedList<Any>()

    override val isBlank: Boolean
        get() = "true" == attributes["nil"]

    override val formParameters: Map<String, String>
        get() {
            val params = LinkedHashMap<String, String>()
            for (node in childNodes()) {
                node.buildParams("", params)
            }
            params.entries
            return params
        }

    override fun findAll(expression: String): List<NodeWrapper> {
        val paths = expression.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val tokens = LinkedList(Arrays.asList(*paths))
        val nodes = LinkedList<NodeWrapper>()
        findAll(tokens, nodes)
        return nodes
    }

    private fun findAll(tokens: LinkedList<String>, nodes: MutableList<NodeWrapper>) {
        if (tokens.isEmpty())
            nodes.add(this)
        else {
            val first = tokens.first
            if ("." == first)
                findAll(restOf(tokens), nodes)
            for (node in childNodes()) {
                if ("*" == first || first == node.elementName)
                    node.findAll(restOf(tokens), nodes)
            }
        }
    }

    private fun find(tokens: LinkedList<String>): SimpleNodeWrapper? {
        if (tokens.isEmpty())
            return this
        else {
            val first = tokens.first
            if ("." == first)
                return find(restOf(tokens))
            for (node in childNodes()) {
                if ("*" == first || first == node.elementName)
                    return node.find(restOf(tokens))
            }
            return null
        }
    }

    private fun find(expression: String): SimpleNodeWrapper? {
        val paths = expression.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val tokens = LinkedList(Arrays.asList(*paths))

        return find(tokens)
    }

    private fun restOf(tokens: LinkedList<String>): LinkedList<String> {
        val newTokens = LinkedList(tokens)
        newTokens.removeFirst()
        return newTokens
    }

    override fun findFirst(expression: String): NodeWrapper? {
        return find(expression)
    }

    override fun findString(expression: String): String? {
        val node = find(expression)
        return node?.stringValue()
    }

    private fun stringValue(): String? {
        if (content.size == 1 && content[0] == null)
            return null
        val value = StringBuilder()
        for (o in content) {
            value.append(o.toString())
        }
        return value.toString().trim { it <= ' ' }
    }

    private fun childNodes(): List<SimpleNodeWrapper> {
        val nodes = LinkedList<SimpleNodeWrapper>()
        for (o in content) {
            if (o is SimpleNodeWrapper)
                nodes.add(o)
        }
        return nodes
    }

    private fun buildParams(prefix: String, params: MutableMap<String, String>) {
        val childNodes = childNodes()
        val newPrefix = if ("" == prefix)
            StringUtils.underscore(elementName + "[" +
                    findString("response_code") + "]")
        else
            prefix + "[" + StringUtils.underscore(elementName) + "]"
        if (childNodes.isEmpty())
            params[newPrefix!!] = stringValue()!!
        else {
            for (childNode in childNodes)
                newPrefix?.let { childNode.buildParams(it, params) }
        }
    }

    override fun toString(): String {
        return ("<" + elementName + (if (attributes.isEmpty()) "" else " attributes=" + StringUtils.toString(attributes))
                + " content=" + StringUtils.toString(content) + ">")
    }

    private class MapNodeHandler : DefaultHandler() {

        private val stack = Stack<SimpleNodeWrapper>()
        lateinit var root: SimpleNodeWrapper

        @Throws(SAXException::class)
        override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
            val node = SimpleNodeWrapper(qName)

            (0 until attributes.length)
                    .forEach { i ->
                        node.attributes[attributes.getQName(i)] = attributes.getValue(i)
                    }

            when {
                "true" == node.attributes["nil"] -> node.content.add("")
            }

            when {
                !stack.isEmpty() -> stack.peek().content.add(node)
            }

            stack.push(node)
        }

        @Throws(SAXException::class)
        override fun endElement(uri: String, localName: String, qName: String) {
            val pop = stack.pop()
            if (stack.isEmpty())
                root = pop
        }

        @Throws(SAXException::class)
        override fun characters(chars: CharArray, start: Int, length: Int) {
            val value = String(chars, start, length)

            val matcher = NON_WHITE_SPACE.matcher(value)
            if (value.length > 0 && matcher.find()) {
                stack.peek().content.add(value)
            }
        }

        companion object {
            private val NON_WHITE_SPACE = Pattern.compile("\\S")
        }
    }

    companion object {

        private val saxParserFactory = SAXParserFactory.newInstance()

        fun parse(xml: String): SimpleNodeWrapper {
            try {
                val source = InputSource(StringReader(xml))
                val parser = saxParserFactory.newSAXParser()
                val handler = MapNodeHandler()
                parser.parse(source, handler)
                return handler.root
            } catch (e: Exception) {
                throw IllegalArgumentException(e.message, e)
            }

        }
    }
}
