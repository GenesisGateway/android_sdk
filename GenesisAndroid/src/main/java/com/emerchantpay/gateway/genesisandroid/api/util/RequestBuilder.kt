package com.emerchantpay.gateway.genesisandroid.api.util

import com.emerchantpay.gateway.genesisandroid.api.interfaces.BillingAddressAttributes
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

open class RequestBuilder(private val parent: String) : BillingAddressAttributes {
    private val topLevelElements: MutableList<Map.Entry<String, String>>
    internal var elements: MutableList<Map.Entry<String, Any>>? = null

    init {
        this.topLevelElements = ArrayList<Map.Entry<String, String>>()
        this.elements = ArrayList<Map.Entry<String, Any>>()
    }

    fun addTopLevelElement(name: String, value: String): RequestBuilder {
        topLevelElements.add(AbstractMap.SimpleEntry(name, value) as Map.Entry<String, String>)
        return this
    }

    fun addElement(value: Any): RequestBuilder {
        elements!!.add(AbstractMap.SimpleEntry("", value))

        return this
    }

    fun addElement(name: String, value: Any): RequestBuilder {
        (0 until elements!!.size!!).forEach() { i ->
            when {

                elements!!.isNotEmpty()
                        && elements!!.size > 0
                        && elements!![i].key == name
                        && name != "reminder" ->
                    elements = arrayListOf()
            }
        }
        elements!!.add(AbstractMap.SimpleEntry(name, value))
        return this
    }

    fun toQueryString(): String {
        val queryString = QueryString()
        topLevelElements.forEach { entry ->
            StringUtils.underscore(entry.key)?.let { queryString.append(it, entry.value) }
        }
        elements!!.forEach { entry ->
            StringUtils.underscore(parent)?.let { StringUtils.underscore(entry.key)?.let { it1 -> parentBracketChildString(it, it1) } }?.let {
                queryString.append(
                        it,
                        entry.value)
            }
        }
        return queryString.toString()
    }

    fun toXML(): String {
        val builder = StringBuilder()
        builder.append(String.format("<%s>", parent))
        elements!!.forEach { entry ->
            builder.append(buildXMLElement(entry.key, entry.value))
        }
        builder.append(String.format("</%s>", parent))

        val findArray = arrayOf("&amp;", "&lt;", "&gt;", "&apos;", "&quot;", "<>", "</>")
        val replaceArray = arrayOf("&", "<", ">", "'", "\"", "", "")

        return replaceAllStrings(findArray, replaceArray, builder.toString())
    }

    fun getElements(): List<Map.Entry<String, Any>>? {
        return elements
    }

    companion object {
        @JvmStatic
        fun buildXMLElement(element: Any): String? {
            return buildXMLElement("", element)
        }

        @JvmStatic
        fun buildXMLElement(name: String, element: Any?): String? {
            when (element) {
                null -> return ""
                is Request -> return element.toXML()
                is Calendar -> {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                    return String.format("<%s type=\"datetime\">%s</%s>", name,
                            dateFormat.format(element.time), name)
                }
                is Map<*, *> -> return formatAsXML(name, element as Map<String, Any>?)
                is List<*> -> {
                    val xml = StringBuilder()
                    for (item in (element as List<Any>?)!!) {
                        xml.append(buildXMLElement("item", item))
                    }
                    return wrapInXMLTag(name, xml.toString(), "array")
                }
                else -> return String.format("<%s>%s</%s>", xmlEscape(name), if (element == null) "" else xmlEscape(element.toString()),
                        xmlEscape(name))
            }
        }

        @JvmStatic
        fun formatAsXML(name: String, map: Map<String, Any>?): String {
            return when (map) {
                null -> ""
                else -> {
                    val xml = StringBuilder()
                    xml.append(String.format("<%s>", name))
                    map.forEach { (key, value) ->
                        xml.append(buildXMLElement(key, value))
                    }
                    xml.append(String.format("</%s>", name))
                    xml.toString()
                }
            }
        }

        fun buildQueryStringElement(name: String, value: String?): Any {
            return when {
                value != null -> try {
                    String.format("%s=%s&", URLEncoder.encode(name, "UTF-8"), URLEncoder.encode(value, "UTF-8"))
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
                else -> ""
            }
        }

        fun parentBracketChildString(parent: String, child: String): String {
            return String.format("%s[%s]", parent, child)
        }

        fun wrapInXMLTag(tagName: String, xml: String): String {
            return String.format("<%s/>%s</%s>", tagName, xml, tagName)
        }

        fun wrapInXMLTag(tagName: String, xml: String, type: String): String {
            return String.format("<%s type=\"%s\">%s</%s>", tagName, type, xml, tagName)
        }

        fun replaceAllStrings(findArr: Array<String>, replaceArr: Array<String>, input: String): String {
            var input = input
            findArr.indices.forEach { i ->
                input = input.replace(findArr[i], replaceArr[i])
            }

            return input
        }

        fun xmlEscape(input: String): String {
            val findArray = arrayOf("&", "<", ">", "'", "\"")
            val replaceArray = arrayOf("&amp;", "&lt;", "&gt;", "&apos;", "&quot;")

            return replaceAllStrings(findArray, replaceArray, input)
        }
    }
}