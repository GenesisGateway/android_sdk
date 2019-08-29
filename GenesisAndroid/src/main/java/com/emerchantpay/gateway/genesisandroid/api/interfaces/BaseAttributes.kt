package com.emerchantpay.gateway.genesisandroid.api.interfaces

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface BaseAttributes {

    // Base Params
    fun setTransactionId(transactionId: String): BaseAttributes {
        requestBuilder.addElement("transaction_id", transactionId)
        return this
    }

    fun setUsage(usage: String): BaseAttributes {
        requestBuilder.addElement("usage", usage)
        return this
    }

    fun setRemoteIp(remoteIP: String): BaseAttributes {
        requestBuilder.addElement("remote_ip", remoteIP)
        return this
    }

    fun buildBaseParams(): RequestBuilder {
        return requestBuilder
    }

    companion object {

        val requestBuilder = RequestBuilder("")
    }
}