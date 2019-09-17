package com.emerchantpay.gateway.genesisandroid.api.internal.request

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.util.Request
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

open class RetrieveConsumerRequest: Request {
    // Request Builder
    private var requestBuilder: RequestBuilder? = null

    private var context: Context? = null
    private var email: String? = null

    constructor(context: Context?, email: String?) {
        this.context = context
        this.email = email
    }

    override fun toXML(): String {
        return buildRequest("retrieve_consumer_request")!!.toXML()
    }

    override fun toQueryString(root: String): String {
        return buildRequest(root)?.toQueryString().toString()
    }

    protected open fun buildRequest(root: String): RequestBuilder? {
        requestBuilder = RequestBuilder(root)
        email?.let { requestBuilder?.addElement("email", it) }
        return requestBuilder
    }

    override fun getTransactionType(): String? {
        return "retrieve_consumer_request"
    }
}
