package com.emerchantpay.gateway.genesisandroid.api.internal.request

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.constants.SharedPrefConstants
import com.emerchantpay.gateway.genesisandroid.api.constants.TransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.constants.URLConstants
import com.emerchantpay.gateway.genesisandroid.api.interfaces.BaseAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.RiskParamsAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.customerinfo.CustomerInfoAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.AsyncAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.DescriptorAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.PaymentAttributes
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress
import com.emerchantpay.gateway.genesisandroid.api.models.RiskParams
import com.emerchantpay.gateway.genesisandroid.api.models.klarna.KlarnaItem
import com.emerchantpay.gateway.genesisandroid.api.util.GenesisSharedPreferences
import com.emerchantpay.gateway.genesisandroid.api.util.KeyStoreUtil
import com.emerchantpay.gateway.genesisandroid.api.util.Request
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder
import java.math.BigDecimal
import java.math.MathContext
import java.util.*
import kotlin.math.pow

open class PaymentRequest : Request, PaymentAttributes, CustomerInfoAttributes, DescriptorAttributes, AsyncAttributes, RiskParamsAttributes {
    // Request Builder
    private var paymentRequestBuilder: RequestBuilder? = null

    // Application context
    private var context: Context? = null

    internal lateinit var transactionId: String
    override var currency: String? = null
    internal var exponent: Int? = 0
    override var amount: BigDecimal? = null
    internal var description: String = ""
    internal var notificationUrl: String = ""
    internal lateinit var cancelUrl: String
    internal var usage: String = ""
    internal var consumerId: String = ""
    internal lateinit var customerEmail: String
    internal lateinit var customerPhone: String
    internal var lifetime: Int = 0
    internal lateinit var customerGender: String
    internal var orderTaxAmount: BigDecimal? = null

    // Pay Later
    internal var payLater: Boolean? = false

    // Crypto and Gaming
    internal var crypto: Boolean? = false
    internal var gaming: Boolean? = false

    // Remember card
    internal var rememberCard: Boolean? = false

    // Payment Addresses
    internal var paymentAddress: PaymentAddress? = null
    internal val shippingAddress: PaymentAddress? = null

    // Transaction types
    internal var transactionTypes = TransactionTypesRequest(this)

    // Risk params
    internal var riskParams: RiskParams? = null

    // Klarna items
    internal var klarnaItemsRequest: KlarnaItemsRequest? = null

    // Reminders
    internal val reminders = RemindersRequest(this)

    // Error handler
    private var error: GenesisError? = null

    // GenesisValidator
    internal val validator = GenesisValidator()

    // Shared preferences
    private val sharedPreferences = GenesisSharedPreferences()

    // Validate
    val isValidData: Boolean?
        get() {
            paymentAddress?.let { validator.isValidAddress(it) }
            validator.isValidConsumerId(consumerId)

            transactionTypes.transactionTypesList.forEach { transactionType ->
                return when (transactionType) {
                    "klarna_authorize" -> orderTaxAmount?.let { amount?.let { it1 -> validator.isValidKlarnaRequest(klarnaItemsRequest, it1, it) } }!! && validator.isValidRequest(this)!!
                    else -> validator.isValidRequest(this)!!
                }
            }

            return null
        }

    val returnSuccessUrl: String
        get() = URLConstants.SUCCESS_URL

    val returnCancelUrl: String
        get() = URLConstants.CANCEL_URL

    @Throws(IllegalAccessException::class)
    constructor(context: Context?, transactionId: String?, amount: BigDecimal?, currency: Currency, customerEmail: String?,
                customerPhone: String?, billingAddress: PaymentAddress?, notificationUrl: String?,
                transactionTypes: TransactionTypesRequest) : super() {

        this.context = context
        this.transactionId = transactionId!!
        this.amount = amount
        this.currency = currency.currency
        this.exponent = currency.exponent
        this.customerEmail = customerEmail!!
        this.customerPhone = customerPhone!!
        this.paymentAddress = billingAddress
        this.transactionTypes = transactionTypes

        when {
            notificationUrl != null && !notificationUrl.isEmpty() -> this.notificationUrl = notificationUrl
        }

        // Init params

        // Load shared preferences

        // Init params
        loadParams()
        setBillingAddress(billingAddress)

        // Load shared preferences
        sharedPreferences.loadSharedPreferences(context, this)
    }

    constructor() {}

    @Throws(IllegalAccessException::class)
    fun setBillingAddress(billingAddress: PaymentAddress?) {
        // Billing Payment Address
        billingAddress?.firstName?.let { setBillingFirstname(it) }
        billingAddress?.lastname?.let { setBillingLastname(it) }
        billingAddress?.address1?.let { setBillingPrimaryAddress(it) }
        billingAddress?.address2?.let { setBillingSecondaryAddress(it) }
        billingAddress?.zipCode?.let { setBillingZipCode(it) }
        billingAddress?.city?.let { setBillingCity(it) }
        billingAddress?.state?.let { setBillingState(it) }
        billingAddress?.countryName?.let { setBillingCountry(it) }
    }

    @Throws(IllegalAccessException::class)
    fun setShippingAddress(shippingAddress: PaymentAddress) {
        // Shipping Payment Address
        setShippingFirstname(shippingAddress?.firstName)
        setShippingLastname(shippingAddress?.lastname)
        setShippingPrimaryAddress(shippingAddress?.address1)
        setShippingSecondaryAddress(shippingAddress?.address2)
        setShippingZipCode(shippingAddress?.zipCode)
        setShippingCity(shippingAddress?.city)
        setShippingState(shippingAddress?.state)
        setShippingCountry(shippingAddress?.countryName)
    }

    fun loadParams() {
        setTransactionId(transactionId)
        setAmount(amount)

        // Set currency
        currency?.let { setCurrency(it) }

        // Customer info
        setCustomerEmail(customerEmail)
        setCustomerPhone(customerPhone)

        // Urls
        when {
            notificationUrl != null && !notificationUrl!!.isEmpty() -> setNotificationUrl(notificationUrl!!)
        }

        setReturnSuccessUrl(URLConstants.SUCCESS_URL)
        setReturnFailureUrl(URLConstants.FAILURE_URL)
        setReturnCancelUrl(URLConstants.CANCEL_URL)
    }

    private fun setAmount(amount: BigDecimal?): PaymentAttributes {
        this.amount = amount
        return this
    }

    private fun setCurrency(currency: String): PaymentAttributes {
        this.currency = currency
        return this
    }

    override fun setUsage(usage: String): BaseAttributes {
        this.usage = usage
        context?.let { sharedPreferences.putString(it, SharedPrefConstants.USAGE_KEY, usage) }
        return this
    }

    fun setDescription(description: String): PaymentRequest {
        this.description = description
        return this
    }

    fun setOrderTax(orderTaxAmount: BigDecimal): PaymentRequest {
        var orderTaxAmount = orderTaxAmount

        orderTaxAmount = when {
            exponent!! > 0 -> {
                val multiplyExp = BigDecimal(10.0.pow(exponent!!), MathContext.DECIMAL64)

                amount?.divide(multiplyExp)!!
            }
            else -> amount!!
        }

        this.orderTaxAmount = orderTaxAmount

        return this
    }

    fun getCustomerEmail(): String {
        return customerEmail
    }

    fun getCustomerPhone(): String {
        return customerPhone
    }

    fun setNotificationUrl(notificationUrl: String): PaymentRequest {
        this.notificationUrl = notificationUrl
        return this
    }

    fun setReturnCancelUrl(cancelUrl: String): PaymentRequest {
        this.cancelUrl = cancelUrl
        return this
    }

    fun setLifetime(lifetime: Int?): PaymentRequest {
        this.lifetime = lifetime!!
        return this
    }

    fun setOrderTaxAmount(orderTaxAmount: BigDecimal?): PaymentRequest {
        this.orderTaxAmount = when {
            exponent!! > 0 -> {
                val multiplyExp = BigDecimal(10.0.pow(exponent!!.toDouble()), MathContext.DECIMAL64)

                amount!!.divide(multiplyExp)
            }
            else -> amount
        }
        return this
    }

    fun setConsumerId(consumerId: String): PaymentRequest {
        this.consumerId = consumerId
        try {
            context?.let {
                sharedPreferences.putString(it, SharedPrefConstants.CONSUMER_ID,
                        KeyStoreUtil(context!!).encryptData(consumerId))
            }
        } catch (e: Exception) {

        }

        return this
    }

    fun setCustomerGender(customerGender: String): PaymentRequest {
        this.customerGender = customerGender
        return this
    }

    fun setPayLater(payLater: Boolean?): PaymentRequest {
        this.payLater = payLater
        return this
    }

    fun setCrypto(crypto: Boolean?): PaymentRequest {
        this.crypto = crypto
        return this
    }

    fun setGaming(gaming: Boolean?): PaymentRequest {
        this.gaming = gaming
        return this
    }

    fun setRememberCard(rememberCard: Boolean?): PaymentRequest {
        this.rememberCard = rememberCard
        return this
    }

    fun setRiskParams(riskParams: RiskParams): PaymentRequest {
        // Set params
        setRiskUserId(riskParams.userId)
        setRiskSessionId(riskParams.sessionId)
        setRiskSSN(riskParams.ssn)
        setRiskMacAddress(riskParams.macAddress)
        setRiskUserLevel(riskParams.userLevel)
        setRiskEmail(riskParams.email)
        setRiskPhone(riskParams.phone)
        setRiskRemoteIp(riskParams.remoteIp)
        setRiskSerialNumber(riskParams.serialNumber)

        this.riskParams = riskParams
        return this
    }

    fun addKlarnaItem(klarnaItem: KlarnaItem): KlarnaItemsRequest {
        klarnaItemsRequest = KlarnaItemsRequest(klarnaItem)
        return klarnaItemsRequest as KlarnaItemsRequest
    }

    fun addKlarnaItems(klarnaItems: ArrayList<KlarnaItem>): KlarnaItemsRequest {
        klarnaItemsRequest = KlarnaItemsRequest(klarnaItems)
        return klarnaItemsRequest as KlarnaItemsRequest
    }

    fun addReminder(channel: String, after: Int?): RemindersRequest {
        return reminders.addReminder(channel, after)
    }

    override fun toXML(): String {
        return buildRequest("wpf_payment")!!.toXML()
    }

    override fun toQueryString(root: String): String {
        return buildRequest(root)!!.toQueryString()
    }

    protected open fun buildRequest(root: String): RequestBuilder? {
        paymentRequestBuilder = RequestBuilder(root)

        when {
            isValidData!! -> {
                paymentRequestBuilder = RequestBuilder(root)
                        .addElement(buildBaseParams().toXML())
                        .addElement(buildPaymentParams().toXML())
                        .addElement(buildCustomerInfoParams().toXML())
                        .addElement("usage", usage)
                        .addElement("description", description)
                        .addElement("notification_url", notificationUrl)
                        .addElement(buildAsyncParams().toXML())
                        .addElement("return_cancel_url", cancelUrl)
                        .addElement("lifetime", lifetime)
                        .addElement("pay_later", payLater!!)
                        .addElement("crypto", crypto!!)
                        .addElement("gaming", gaming!!)
                        .addElement("remember_card", rememberCard!!)
                        .addElement("billing_address", buildBillingAddress()?.toXML())
                        .addElement("shipping_address", buildShippingAddress()?.toXML())
                        .addElement("transaction_types", transactionTypes)
                        .addElement("risk_params", buildRiskParams().toXML())
                        .addElement("dynamic_descriptor_params", buildDescriptorParams().toXML())

                consumerId = getConsumerId()

                when {
                    transactionTypes.transactionTypesList.contains(TransactionTypes.KLARNA_AUTHORIZE) -> orderTaxAmount?.let {
                        paymentRequestBuilder!!.addElement("customer_gender", customerGender)
                                .addElement("order_tax_amount", it)
                    }
                }

                when {
                    klarnaItemsRequest != null -> paymentRequestBuilder!!.addElement(klarnaItemsRequest!!.toXML())
                }

                // Pay Later
                if (payLater == true && reminders.error == null) {
                    paymentRequestBuilder!!.addElement("reminders", reminders)
                } else {
                    error = reminders.error
                }

                return paymentRequestBuilder
            }
            else -> return RequestBuilder(root)
        }
    }

    fun getError(): GenesisError? {
        if (validator.error != null) {
            error = validator.error
        }

        return error
    }

    fun getNotificationUrl(): String? {
        return notificationUrl
    }

    fun getUsage(): String? {
        return usage
    }

    fun getPayLater(): Boolean? {
        return payLater
    }

    fun getCrypto(): Boolean {
        return crypto!!
    }

    fun getGaming(): Boolean {
        return gaming!!
    }

    fun getRememberCard(): Boolean {
        return rememberCard!!
    }

    fun getConsumerId(): String {
        try {
            when {
                consumerId != null && consumerId!!.isNotEmpty() -> paymentRequestBuilder!!.addElement(SharedPrefConstants.CONSUMER_ID, consumerId!!)
                else -> {
                    consumerId = context?.let {
                        KeyStoreUtil(it)
                                .decryptData(sharedPreferences.getString(context!!, SharedPrefConstants.CONSUMER_ID))
                    }.toString()
                    consumerId?.let { paymentRequestBuilder!!.addElement(SharedPrefConstants.CONSUMER_ID, it) }
                }
            }
        } catch (e: Exception) {
            consumerId = "0"
        }

        return consumerId
    }

    fun getKlarnaItemsRequest(paymentRequest: PaymentRequest): KlarnaItemsRequest? {
        return paymentRequest.klarnaItemsRequest
    }

    override fun getTransactionType(): String? {
        return "wpf_payment"
    }

    companion object {
        fun getAmount(paymentRequest: PaymentRequest): BigDecimal? {
            return paymentRequest.amount
        }

        fun getCurrency(paymentRequest: PaymentRequest): String? {
            return paymentRequest.currency
        }
    }
}
