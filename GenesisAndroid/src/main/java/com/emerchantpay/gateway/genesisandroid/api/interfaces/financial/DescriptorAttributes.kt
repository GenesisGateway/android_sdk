package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface DescriptorAttributes {

    // Descriptor Params
    fun setDynamicMerchantName(dynamicMerchantName: String): DescriptorAttributes {
        requestBuilder.addElement("merchant_name", dynamicMerchantName)
        return this
    }

    fun setDynamicMerchantCity(dynamicMerchantCity: String): DescriptorAttributes {
        requestBuilder.addElement("merchant_city", dynamicMerchantCity)
        return this
    }

    fun buildDescriptorParams(): RequestBuilder {
        return requestBuilder
    }

    companion object {

        val requestBuilder = RequestBuilder("")
    }
}