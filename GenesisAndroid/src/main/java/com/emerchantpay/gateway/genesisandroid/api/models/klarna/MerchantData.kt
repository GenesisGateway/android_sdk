package com.emerchantpay.gateway.genesisandroid.api.models.klarna

import com.emerchantpay.gateway.genesisandroid.api.util.Request
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

class MerchantData : Request() {

    private val builder = RequestBuilder("merchant_data")

    private var marketplaceSellerInfo: String? = null

    val merchantDataList: MutableList<Map.Entry<String, Any>>?
        get() = builder.elements

    fun setMarketSellerInfo(marketplaceSellerInfo: String): MerchantData {
        this.marketplaceSellerInfo = marketplaceSellerInfo
        return this
    }

    fun addMerchantDataParam(key: String, value: String): MerchantData {
        builder.addElement(key, value)
        return this
    }

    fun addMerchantDataParams(merchantDataMap: Map<String, String>): MerchantData {
        for ((key, value) in merchantDataMap) {
            builder.addElement(key, value)
        }

        return this
    }

    override fun toXML(): String? {
        return buildRequest()?.toXML()
    }

    override fun toQueryString(): String? {
        return buildRequest()?.toQueryString()
    }

    protected fun buildRequest(): RequestBuilder? {
        return marketplaceSellerInfo?.let { builder.addElement("marketplace_seller_info", it) }
    }
}
