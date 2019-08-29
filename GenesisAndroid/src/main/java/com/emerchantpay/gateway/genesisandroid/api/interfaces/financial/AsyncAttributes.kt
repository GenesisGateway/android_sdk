package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface AsyncAttributes {

    // Async Params
    fun setReturnSuccessUrl(successUrl: String): AsyncAttributes {
        requestBuilder.addElement("return_success_url", successUrl)
        return this
    }

    fun setReturnFailureUrl(failureUrl: String): AsyncAttributes {
        requestBuilder.addElement("return_failure_url", failureUrl)
        return this
    }

    fun buildAsyncParams(): RequestBuilder {
        return requestBuilder
    }

    companion object {

        val requestBuilder = RequestBuilder("")
    }
}
