package com.emerchantpay.gateway.genesisandroid.api.util

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class QueryString {
    private val builder: StringBuilder

    constructor() {
        builder = StringBuilder("")
    }

    constructor(content: String) {
        builder = StringBuilder(content)
    }

    fun append(key: String, value: Any?): QueryString {
        return when (value) {
            null -> this
            is Request -> appendRequest(key, value as Request?)
            is Map<*, *> -> appendMap(key, value as Map<*, *>?)
            else -> appendString(key, value.toString())
        }
    }

    fun appendWithoutEncoding(key: String?, value: Any?): QueryString {
        when {
            key != null && key != "" && value != null -> {
                when {
                    builder.isNotEmpty() -> builder.append("&")
                }
                builder.append("$key=$value")
            }
        }
        return this
    }

    fun appendEncodedData(alreadyEncodedData: String?): QueryString {
        when {
            alreadyEncodedData != null && alreadyEncodedData.isNotEmpty() -> {
                builder.append('&')
                builder.append(alreadyEncodedData)
            }
        }
        return this
    }


    override fun toString(): String {
        return builder.toString()
    }

    protected fun appendString(key: String?, value: String?): QueryString {
        if (key != null && key != "" && value != null) {
            if (builder.isNotEmpty()) {
                builder.append("&")
            }
            builder.append(encodeParam(key, value))
        }
        return this
    }

    protected fun appendRequest(parent: String, request: Request?): QueryString {
        when {
            request != null -> {
                val requestQueryString = request.toQueryString(parent)
                when {
                    requestQueryString.isNotEmpty() -> {
                        when {
                            builder.isNotEmpty() -> builder.append("&")
                        }
                        builder.append(requestQueryString)
                    }
                }
            }
        }
        return this
    }

    protected fun appendMap(key: String, value: Map<*, *>?): QueryString {
        for (keyString in value?.keys!!) {
            appendString(String.format("%s[%s]", key, keyString), value?.get(keyString)!!.toString())
        }
        return this
    }

    companion object {

        fun encodeParam(key: String, value: String): String {
            val encodedKey = encode(key)
            val encodedValue = encode(value)
            return "$encodedKey=$encodedValue"
        }

        var DEFAULT_ENCODING = "UTF-8"

        fun encode(value: String): String {
            try {
                return URLEncoder.encode(value, DEFAULT_ENCODING)
            } catch (e: UnsupportedEncodingException) {
                throw IllegalStateException("$DEFAULT_ENCODING encoding should always be available")
            }

        }
    }
}
