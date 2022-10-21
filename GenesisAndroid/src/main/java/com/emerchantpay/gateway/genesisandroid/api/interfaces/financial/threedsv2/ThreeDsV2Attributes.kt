package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface ThreeDsV2Attributes : ThreeDsV2ControlAttributes, ThreeDsV2PurchaseAttributes,
    ThreeDsV2MerchantRiskAttributes, ThreeDsV2CardHolderAccountAttributes, ThreeDsV2RecurringAttributes {

    fun buildThreeDsV2Attributes(): RequestBuilder = with(requestBuilder) {
        addElement("control", buildControlAttributes().toXML())
        addElement("purchase", buildPurchaseAttributes().toXML())
        addElement("merchant_risk", buildMerchantRiskAttributes().toXML())
        addElement("card_holder_account", buildCardHolderAccountAttributes().toXML())
        addElement("recurring", buildRecurringAttributes().toXML())

        return this
    }

    companion object {
        private val requestBuilder = RequestBuilder("")
    }
}