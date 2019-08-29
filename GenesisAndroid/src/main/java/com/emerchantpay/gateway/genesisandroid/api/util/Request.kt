package com.emerchantpay.gateway.genesisandroid.api.util


import com.emerchantpay.gateway.genesisandroid.api.interfaces.AddressAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.BaseAttributes

import java.io.Serializable

/**
 * Abstract class for fluent interface request builders.
 */
abstract class Request : BaseAttributes, AddressAttributes, Serializable {

    val kind: String?
        get() = null

    val request: Request
        get() = this

    open fun getTransactionType(): String? {
        return null
    }

    open fun toXML(): String? {
        throw UnsupportedOperationException()
    }

    open fun toQueryString(parent: String): String {
        throw UnsupportedOperationException()
    }

    open fun toQueryString(): String? {
        throw UnsupportedOperationException()
    }

    protected fun buildXMLElement(element: Any): String? {
        return RequestBuilder.buildXMLElement(element)
    }

    protected fun buildXMLElement(name: String, element: Any): String? {
        return RequestBuilder.buildXMLElement(name, element)
    }
}
