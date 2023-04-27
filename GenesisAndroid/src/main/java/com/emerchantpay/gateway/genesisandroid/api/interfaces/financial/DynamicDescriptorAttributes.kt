package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface DynamicDescriptorAttributes {
    fun setMerchantName(merchantName: String): DynamicDescriptorAttributes {
        requestBuilder.addElement("merchant_name", merchantName.take(MERCHANT_NAME_LENGTH))
        return this
    }

    fun setMerchantCity(merchantCity: String): DynamicDescriptorAttributes {
        requestBuilder.addElement("merchant_city", merchantCity.take(MERCHANT_CITY_LENGTH))
        return this
    }

    fun setSubMerchantId(subMerchantId: String): DynamicDescriptorAttributes {
        requestBuilder.addElement("sub_merchant_id", subMerchantId.take(SUB_MERCHANT_ID_LENGTH))
        return this
    }

    fun buildDescriptorParams(): RequestBuilder {
        return requestBuilder
    }

    companion object {
        private val requestBuilder = RequestBuilder("")

        private const val MERCHANT_NAME_LENGTH = 25
        private const val MERCHANT_CITY_LENGTH = 13
        private const val SUB_MERCHANT_ID_LENGTH = 15
    }
}