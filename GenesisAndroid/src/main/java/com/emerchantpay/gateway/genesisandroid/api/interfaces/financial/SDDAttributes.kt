package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface SDDAttributes {

    // SDD Params
    fun setIban(iban: String): SDDAttributes {
        requestBuilder.addElement("iban", iban)
        return this
    }

    fun setBic(bic: String): SDDAttributes {
        requestBuilder.addElement("bic", bic)
        return this
    }

    fun buildSDDParams(): RequestBuilder {
        return requestBuilder
    }

    companion object {

        val requestBuilder = RequestBuilder("")
    }
}
