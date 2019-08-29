package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial

import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

import java.math.BigDecimal

interface PaymentAttributes {

    val amount: BigDecimal?
        get() = null

    val currency: String?
        get() = ""

    fun buildPaymentParams(): RequestBuilder {

        var convertedAmount: BigDecimal? = null

        when {
            amount != null && currency != null -> {

                val curr = Currency()

                curr.setAmountToExponent(amount!!, currency!!)
                convertedAmount = curr.amount
            }
        }

        requestBuilder.addElement("amount", convertedAmount!!)
                .addElement("currency", currency!!)

        return requestBuilder
    }

    companion object {

        val requestBuilder = RequestBuilder("")
    }
}
