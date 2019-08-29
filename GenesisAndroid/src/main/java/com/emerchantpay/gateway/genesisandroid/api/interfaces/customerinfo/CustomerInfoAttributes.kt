package com.emerchantpay.gateway.genesisandroid.api.interfaces.customerinfo

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface CustomerInfoAttributes {

    fun setCustomerEmail(customerEmail: String): CustomerInfoAttributes {
        requestBuilder.addElement("customer_email", customerEmail)
        return this
    }

    fun setCustomerPhone(customerPhone: String): CustomerInfoAttributes {
        requestBuilder.addElement("customer_phone", customerPhone)
        return this
    }

    fun buildCustomerInfoParams(): RequestBuilder {
        return requestBuilder
    }

    companion object {
        val requestBuilder = RequestBuilder("")
    }
}