package com.emerchantpay.gateway.genesisandroid.api.internal.request


import com.emerchantpay.gateway.genesisandroid.api.constants.WPFTransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.recurring.CommonManagedRecurringAttributes
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator
import com.emerchantpay.gateway.genesisandroid.api.util.Request
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilderWithAttribute
import java.util.*

class TransactionTypesRequest : Request, CommonManagedRecurringAttributes {

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

    private fun <T> addTransaction(t: T): TransactionTypesRequest {
        val transactionType = t.toString()

        // validate transaction type
        validator.validateTransactionType(transactionType)

        transactionTypesList.add(transactionType)

        customAttributes = CustomAttributesRequest(this, transactionType)
        customAttributesList.add(customAttributes!!)

        return this
    }

    @Throws(IllegalAccessException::class)
    fun addTransaction(transactionType: String): TransactionTypesRequest {
        return addTransaction<String>(transactionType)
    }

    @Throws(IllegalAccessException::class)
    fun addTransactions(vararg transactionTypes: String): TransactionTypesRequest {
        transactionTypes.forEach {transactionType ->
            addTransaction(transactionType)
        }

        return this
    }

    @Throws(IllegalAccessException::class)
    fun addTransaction(transactionType: WPFTransactionTypes): TransactionTypesRequest {
        return addTransaction<WPFTransactionTypes>(transactionType)
    }

    @Throws(IllegalAccessException::class)
    fun addTransactions(vararg transactionTypes: WPFTransactionTypes): TransactionTypesRequest {
        transactionTypes.forEach {transactionType ->
            addTransaction(transactionType)
        }

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

    private fun isManagedRecurringEnabled(): Boolean {
        return (transactionTypesList.contains(WPFTransactionTypes.AUTHORIZE.value)
                || transactionTypesList.contains(WPFTransactionTypes.AUTHORIZE3D.value)
                || transactionTypesList.contains(WPFTransactionTypes.SALE.value)
                || transactionTypesList.contains(WPFTransactionTypes.SALE3D.value))
    }

    protected fun buildRequest(root: String): RequestBuilderWithAttribute {

        val builder = RequestBuilderWithAttribute(root, "")

        customAttributesList.forEach { attribute ->
            builder.addElement("", attribute)
        }

        if (isManagedRecurringEnabled())
                builder.addElement("managed_recurring", buildManagedRecurringAttributes().toXML())

        return builder
    }
}
