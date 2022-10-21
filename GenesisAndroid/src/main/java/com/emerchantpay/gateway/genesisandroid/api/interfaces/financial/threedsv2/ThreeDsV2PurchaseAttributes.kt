package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2PurchaseCategory
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface ThreeDsV2PurchaseAttributes {
    fun setCategory(category: ThreeDsV2PurchaseCategory?) = apply {
        category?.let { requestBuilder.addElement("category", it.name.lowercase()) }
    }

    fun buildPurchaseAttributes(): RequestBuilder = requestBuilder

    companion object {
        private val requestBuilder = RequestBuilder("")
    }
}