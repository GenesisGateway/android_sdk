package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.*
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder
import java.util.*
import java.text.SimpleDateFormat

interface ThreeDsV2MerchantRiskAttributes {
    fun setShippingIndicator(shippingIndicator: ThreeDsV2MerchantRiskShippingIndicator?) = apply {
        shippingIndicator?.let { requestBuilder.addElement("shipping_indicator",  it.toString().lowercase()) }
    }

    fun setDeliveryTimeframe(deliveryTimeframe: ThreeDsV2MerchantRiskDeliveryTimeframe?) = apply {
        deliveryTimeframe?.let { requestBuilder.addElement("delivery_timeframe", it.toString().lowercase()) }
    }

    fun setReorderItemsIndicator(reorderItemsIndicator: ThreeDsV2MerchantRiskReorderItemsIndicator?) = apply {
        reorderItemsIndicator?.let { requestBuilder.addElement("reorder_items_indicator", it.toString().lowercase()) }
    }

    fun setPreorderPurchaseIndicator(preorderPurchaseIndicator: ThreeDsV2MerchantRiskPreorderPurchaseIndicator?) = apply {
        preorderPurchaseIndicator?.let { requestBuilder.addElement("pre_order_purchase_indicator", it.toString().lowercase()) }
    }

    fun setPreorderDate(preorderDate: Date?) = apply {
        preorderDate?.let { requestBuilder.addElement("pre_order_date", SimpleDateFormat(DATE_FORMAT).format(it)) }
    }

    fun setIsGiftCard(value: Boolean?) = apply {
        value?.let { requestBuilder.addElement("gift_card", it.toString()) }
    }

    fun setGiftCardCount(count: Int?) = apply {
        count?.let { requestBuilder.addElement("gift_card_count", it.toString()) }
    }

    fun buildMerchantRiskAttributes(): RequestBuilder = requestBuilder

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy"
        private val requestBuilder = RequestBuilder("")
    }
}