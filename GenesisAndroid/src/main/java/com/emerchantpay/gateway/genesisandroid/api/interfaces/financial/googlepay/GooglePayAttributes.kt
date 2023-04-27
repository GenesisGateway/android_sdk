package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.googlepay

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.googlepay.definitions.GooglePayPaymentSubtype
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface GooglePayAttributes {
    fun setPaymentSubtype(paymentSubtype: GooglePayPaymentSubtype): GooglePayAttributes = apply {
        requestBuilder.addElement(PAYMENT_SUBTYPE, paymentSubtype.toString())
    }

    fun buildGooglePayParams(): RequestBuilder {
        return requestBuilder
    }

    companion object {
        private val requestBuilder = RequestBuilder("")

        const val PAYMENT_SUBTYPE = "payment_subtype"
    }
}