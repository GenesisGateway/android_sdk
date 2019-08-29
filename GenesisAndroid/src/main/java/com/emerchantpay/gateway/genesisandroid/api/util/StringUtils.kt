package com.emerchantpay.gateway.genesisandroid.api.util

import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class StringUtils {

    internal lateinit var uid: String

    fun generateUID(): String {
        val uuid = UUID.randomUUID()
        uid = uuid.toString()
        uid = uid.replace("-", "").substring(0, 30)

        return uid
    }

    companion object {

        fun <T> classToXMLName(klass: Class<T>): String {
            return dasherize(klass.simpleName)!!.toLowerCase()
        }

        fun dasherize(str: String?): String? {
            return str?.replace("([A-Z]+)([A-Z][a-z])".toRegex(), "$1-$2")?.replace("([a-z])([A-Z])".toRegex(), "$1-$2")?.replace("_".toRegex(), "-")?.toLowerCase()

        }

        fun getFullPathOfFile(filename: String): String {
            return classLoader.getResource(filename).file
        }

        private val classLoader: ClassLoader
            get() = Thread.currentThread().contextClassLoader

        @Throws(IOException::class)
        fun inputStreamToString(inputStream: InputStream): String {
            val inputReader = InputStreamReader(inputStream)
            val builder = StringBuilder()
            val buffer = CharArray(0x1000)
            var bytesRead = inputReader.read(buffer, 0, buffer.size)
            while (bytesRead >= 0) {
                builder.append(buffer, 0, bytesRead)
                bytesRead = inputReader.read(buffer, 0, buffer.size)
            }
            return builder.toString()
        }

        fun nullIfEmpty(str: String?): String? {
            return if (str == null || str.length == 0) null else str
        }

        fun underscore(str: String?): String? {
            return str?.replace("([A-Z]+)([A-Z][a-z])".toRegex(), "$1_$2")?.replace("([a-z])([A-Z])".toRegex(), "$1_$2")?.replace("-".toRegex(), "_")?.toLowerCase()

        }

        fun joinTokens(tokens: Array<Any>, delimiter: String): String {
            when {
                tokens.isEmpty() -> return ""
                else -> {
                    val joined = StringBuilder()

                    var first = true
                    for (token in tokens) {
                        if (!first)
                            joined.append(delimiter)
                        else
                            first = false
                        joined.append(token)
                    }

                    return joined.toString()
                }
            }
        }

        fun join(delimiter: String, vararg tokens: Any): String {
            return joinTokens(tokens as Array<Any>, delimiter)
        }

        fun mapToString(map: Map<String, Any>): String {
            val pairs = LinkedList<String>()
            val keyList = ArrayList(map.keys)
            Collections.sort(keyList)
            for (s in keyList) {
                val value = map[s]
                val valueStr = toString(value)
                pairs.add("$s: $valueStr")
            }

            return "{" + join(", ", *pairs.toTypedArray()) + "}"
        }

        fun toString(value: Any?): String {
            return if (value is Map<*, *>)
                mapToString((value as Map<String, Any>?)!!)
            else if (value is List<*>)
                listToString((value as List<Any>?)!!)
            else value?.toString()?.trim { it <= ' ' } ?: "null"
        }

        fun listToString(value: List<Any>): String {
            val valueStrings = arrayOfNulls<Any>(value.size)
            for (i in valueStrings.indices) {
                valueStrings[i] = toString(value[i])
            }
            return "[" + join(", ", *arrayOf(valueStrings)) + "]"
        }

        fun unescapeUtf8(encodedString: String): String {
            var i = 0
            val len = encodedString.length
            var c: Char
            val buffer = StringBuffer(len)

            while (i < len) {
                c = encodedString[i++]
                if (c == '\\') {
                    if (i < len) {
                        c = encodedString[i++]
                        if (c == 'u') {
                            c = Integer.parseInt(encodedString.substring(i, i + 4), 16).toChar()
                            i += 4
                        }
                    }
                }
                buffer.append(c)
            }
            return buffer.toString()
        }
    }
}
