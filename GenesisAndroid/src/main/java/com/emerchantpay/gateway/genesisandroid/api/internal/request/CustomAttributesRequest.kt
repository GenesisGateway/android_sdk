package com.emerchantpay.gateway.genesisandroid.api.internal.request

import com.emerchantpay.gateway.genesisandroid.api.util.Request
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilderWithAttribute
import java.util.*

class CustomAttributesRequest : Request {

    private var parent: TransactionTypesRequest? = null

    private lateinit var transactionType: String

    val paramsMap = HashMap<String, String>()

    constructor() : super() {}

    constructor(parent: TransactionTypesRequest, transactionType: String) {
        this.parent = parent
        this.transactionType = transactionType
    }

    fun addAttribute(key: String, value: String): CustomAttributesRequest {
        paramsMap[key] = value
        return this
    }

    override fun toXML(): String {
        return buildRequest("transaction_type").toXML()
    }

    override fun toQueryString(root: String): String {
        return buildRequest(root).toQueryString()
    }

    protected fun buildRequest(root: String): RequestBuilderWithAttribute {

        val builder: RequestBuilderWithAttribute = transactionType?.let { RequestBuilderWithAttribute(root, it) }!!

        paramsMap.keys.forEach { key ->
            paramsMap[key]?.let { builder.addElement(key, it) }
        }

        return builder
    }

    override fun getTransactionType(): String {
        return transactionType
    }

    fun done(): TransactionTypesRequest? {
        return parent
    }
}
