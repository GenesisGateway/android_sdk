package com.emerchantpay.gateway.genesisandroid.api.internal.request


import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator
import com.emerchantpay.gateway.genesisandroid.api.util.Request
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder
import java.util.*

class TransactionTypesRequest : Request {

    private var parent: PaymentRequest? = null
    val transactionTypesList = ArrayList<String>()
    var customAttributes: CustomAttributesRequest? = null
        private set
    val customAttributesList = ArrayList<CustomAttributesRequest>()

    // Genesis Validator
    private val validator = GenesisValidator()

    constructor() : super() {}

    constructor(parent: PaymentRequest) {
        this.parent = parent
    }

    @Throws(IllegalAccessException::class)
    fun addTransaction(transactionType: String): TransactionTypesRequest {
        // validate transaction type
        validator.validateTransactionType(transactionType)

        transactionTypesList.add(transactionType)

        customAttributes = CustomAttributesRequest(this, transactionType)
        customAttributesList.add(customAttributes!!)

        return this
    }

    @Throws(IllegalAccessException::class)
    fun addTransaction(transactionType: String, additionalParams: HashMap<String, String>): TransactionTypesRequest {
        // validate transaction type
        validator.validateTransactionType(transactionType)

        transactionTypesList.add(transactionType)

        customAttributes = CustomAttributesRequest(this, transactionType)

        additionalParams.keys.forEach { key ->
            additionalParams[key]?.let { customAttributes!!.addAttribute(key, it) }
        }

        customAttributesList.add(customAttributes!!)

        return this
    }

    fun addParam(key: String, value: String): TransactionTypesRequest {
        this.customAttributes!!.addAttribute(key, value)
        return this
    }

    override fun toXML(): String {
        return buildRequest("transaction_types").toXML()
    }

    override fun toQueryString(root: String): String {
        return buildRequest(root).toQueryString()
    }

    protected fun buildRequest(root: String): RequestBuilder {

        val builder = RequestBuilder(root)

        customAttributesList.forEach { attribute ->
            builder.addElement("", attribute)
        }

        return builder
    }

    fun done(): PaymentRequest? {
        return parent
    }
}
