package com.emerchantpay.gateway.genesisandroid.api.internal.request

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.constants.SharedPrefConstants
import com.emerchantpay.gateway.genesisandroid.api.constants.URLConstants
import com.emerchantpay.gateway.genesisandroid.api.constants.WPFTransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.constants.recurring.RecurringCategory
import com.emerchantpay.gateway.genesisandroid.api.constants.recurring.RecurringType
import com.emerchantpay.gateway.genesisandroid.api.interfaces.BaseAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.RiskParamsAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.customerinfo.CustomerInfoAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.AsyncAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.DynamicDescriptorAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.PaymentAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.googlepay.GooglePayAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.googlepay.definitions.GooglePayPaymentSubtype
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.ThreeDsV2Attributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2ControlDeviceType
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator
import com.emerchantpay.gateway.genesisandroid.api.models.*
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.models.klarna.KlarnaItem
import com.emerchantpay.gateway.genesisandroid.api.models.threedsv2.ThreeDsV2Params
import com.emerchantpay.gateway.genesisandroid.api.util.GenesisSharedPreferences
import com.emerchantpay.gateway.genesisandroid.api.util.KeyStoreUtil
import com.emerchantpay.gateway.genesisandroid.api.util.Request
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder
import java.math.BigDecimal
import java.math.MathContext
import java.util.*
import kotlin.math.pow

open class PaymentRequest : Request, PaymentAttributes, CustomerInfoAttributes, DynamicDescriptorAttributes,
    AsyncAttributes, RiskParamsAttributes, ThreeDsV2Attributes, GooglePayAttributes {
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
    internal var consumerId: String? = ""
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

    // 3DSv2 params
    internal var threeDsV2Params: ThreeDsV2Params? = null

    // Klarna items
    internal var klarnaItemsRequest: KlarnaItemsRequest? = null

    // Reminders
    internal val reminders = RemindersRequest(this)

    // Error handler
    private var error: GenesisError? = null

    // GenesisValidator
    internal var validator: GenesisValidator? = null

    // Shared preferences
    private val sharedPreferences = GenesisSharedPreferences()

    // Validate
    val isValidData: Boolean?
        get() {
            validator = GenesisValidator()
            paymentAddress?.let { validator?.isValidAddress(it) }
            validator?.isValidConsumerId(consumerId)

            transactionTypes.transactionTypesList.forEach { transactionType ->
                return when (transactionType) {
                    "klarna_authorize" -> orderTaxAmount?.let {
                        amount?.let { it1 ->
                            validator?.isValidKlarnaRequest(klarnaItemsRequest, it1, it)
                        }
                    }!! && validator?.isValidRequest(this)!!

                    "authorize3d", "sale3d", "init_recurring_sale3d" ->
                        validator?.isValidThreeDsV2Request(this)!! && validator?.isValidRequest(this)!!

                    WPFTransactionTypes.GOOGLE_PAY.value ->
                        validator?.isValidGooglePayRequest(this)!! && validator?.isValidRequest(this)!!

                    else -> validator?.isValidRequest(this)!!
                }
            }

            return null
        }

    val returnSuccessUrl: String
        get() = URLConstants.SUCCESS_URL

    val returnCancelUrl: String
        get() = URLConstants.CANCEL_URL

    // Recurring
    private var recurringType: String? = null
    private var recurringCategory: String? = null

    // Google Pay
    internal var googlePayPaymentSubtype: GooglePayPaymentSubtype? = null

    @Throws(IllegalAccessException::class)
    constructor(context: Context?, transactionId: String?, amount: BigDecimal?, currency: Currency, customerEmail: String?,
                customerPhone: String?, billingAddress: PaymentAddress?, notificationUrl: String?,
                transactionTypes: TransactionTypesRequest) : super() {

        this.context = context
        this.transactionId = transactionId!!
        this.amount = amount
        this.currency = currency.currency
        this.exponent = currency.exponent
        this.customerEmail = customerEmail?: ""
        this.customerPhone = customerPhone?: ""
        this.paymentAddress = billingAddress
        this.transactionTypes = transactionTypes

        when {
            notificationUrl != null && notificationUrl.isNotBlank() -> this.notificationUrl = notificationUrl
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
        setShippingFirstname(shippingAddress.firstName)
        setShippingLastname(shippingAddress.lastname)
        shippingAddress.address1?.let { setShippingPrimaryAddress(it) }
        shippingAddress.address2?.let { setShippingSecondaryAddress(it) }
        shippingAddress.zipCode?.let { setShippingZipCode(it) }
        shippingAddress.city?.let { setShippingCity(it) }
        setShippingState(shippingAddress.state)
        setShippingCountry(shippingAddress.countryName)
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
            notificationUrl != null && notificationUrl!!.isNotEmpty() -> setNotificationUrl(notificationUrl!!)
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

    fun setThreeDsV2Params(threeDsV2Params: ThreeDsV2Params) = apply {
        // Control attributes
        setDeviceType(ThreeDsV2ControlDeviceType.APPLICATION)
        setChallengeIndicator(threeDsV2Params.controlChallengeIndicator)

        // Purchase attributes
        threeDsV2Params.purchaseCategory?.let { setCategory(it) }

        // Merchant risk attributes
        setShippingIndicator(threeDsV2Params.merchantRisk?.shippingIndicator)
        setDeliveryTimeframe(threeDsV2Params.merchantRisk?.deliveryTimeframe)
        setReorderItemsIndicator(threeDsV2Params.merchantRisk?.reorderItemsIndicator)
        setPreorderPurchaseIndicator(threeDsV2Params.merchantRisk?.preorderPurchaseIndicator)
        setPreorderDate(threeDsV2Params.merchantRisk?.preorderDate)
        setIsGiftCard(threeDsV2Params.merchantRisk?.isGiftCard)
        setGiftCardCount(threeDsV2Params.merchantRisk?.giftCardCount)

        // Card holder account attributes
        setCreationDate(threeDsV2Params.cardHolderAccount?.creationDate)
        setUpdateIndicator(threeDsV2Params.cardHolderAccount?.updateIndicator)
        setLastChangeDate(threeDsV2Params.cardHolderAccount?.lastChangeDate)
        setPasswordChangeIndicator(threeDsV2Params.cardHolderAccount?.passwordChangeIndicator)
        setPasswordChangeDate(threeDsV2Params.cardHolderAccount?.passwordChangeDate)
        setShippingAddressUsageIndicator(threeDsV2Params.cardHolderAccount?.shippingAddressUsageIndicator)
        setShippingAddressDateFirstUsed(threeDsV2Params.cardHolderAccount?.shippingAddressDateFirstUsed)
        setTransactionsActivityLast24Hours(threeDsV2Params.cardHolderAccount?.transactionsActivityLast24Hours)
        setTransactionsActivityPreviousYear(threeDsV2Params.cardHolderAccount?.transactionsActivityPreviousYear)
        setProvisionAttemptsLast24Hours(threeDsV2Params.cardHolderAccount?.provisionAttemptsLast24Hours)
        setPurchasesCountLast6Months(threeDsV2Params.cardHolderAccount?.purchasesCountLast6Months)
        setSuspiciousActivityIndicator(threeDsV2Params.cardHolderAccount?.suspiciousActivityIndicator)
        setRegistrationIndicator(threeDsV2Params.cardHolderAccount?.registrationIndicator)
        setRegistrationDate(threeDsV2Params.cardHolderAccount?.registrationDate)

        // Recurring attributes
        setExpirationDate(threeDsV2Params.recurring?.expirationDate)
        setFrequency(threeDsV2Params.recurring?.frequency)

        this.threeDsV2Params = threeDsV2Params
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
    
    fun setGooglePayPaymentSubtype(googlePayPaymentSubtype: GooglePayPaymentSubtype?) {
        this.googlePayPaymentSubtype = googlePayPaymentSubtype
        googlePayPaymentSubtype?.let { setPaymentSubtype(it) }
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
                    .addElement("billing_address", buildBillingAddress().toXML())
                    .addElement("shipping_address", buildShippingAddress().toXML())
                    .addElement("transaction_types", transactionTypes)
                    .addElement("risk_params", buildRiskParams().toXML())
                    .addElement("dynamic_descriptor_params", buildDescriptorParams().toXML())
                    .addElement(buildGooglePayParams().toXML())

                val transactionTypesList = transactionTypes.transactionTypesList

                if (canAddThreeDsV2Params(transactionTypesList)) {
                    paymentRequestBuilder?.addElement("threeds_v2_params", buildThreeDsV2Attributes().toXML())
                }

                if (isRecurringEnabled(transactionTypesList)) {
                    recurringType?.let {
                        transactionTypes.customAttributes?.addAttribute("recurring_type", it)
                    }
                    recurringCategory?.let {
                        transactionTypes.customAttributes?.addAttribute("recurring_category", it)
                    }
                }

                if (!isRecurringEnabled(transactionTypesList))
                    addConsumerId()

                when {
                    transactionTypes.transactionTypesList.contains(WPFTransactionTypes.KLARNA_AUTHORIZE.value) -> orderTaxAmount?.let {
                        paymentRequestBuilder!!.addElement("customer_gender", customerGender)
                                .addElement("order_tax_amount", it)
                    }
                }

                when {
                    klarnaItemsRequest != null -> paymentRequestBuilder!!.addElement(klarnaItemsRequest!!.toXML())
                }

                // Pay Later
                when {
                    payLater == true && reminders.error == null ->
                        paymentRequestBuilder!!.addElement("reminders", reminders)
                    else -> error = reminders.error
                }

                return paymentRequestBuilder
            }
            else -> return RequestBuilder(root)
        }
    }

    private fun canAddThreeDsV2Params(transactionTypesList: ArrayList<String>) =
        transactionTypesList.none { it == WPFTransactionTypes.GOOGLE_PAY.value }

    fun getError(): GenesisError? {
        when {
            validator?.error != null -> error = validator?.error
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

    private fun addConsumerId() {
        if (consumerId?.isNullOrBlank() == false)
            paymentRequestBuilder!!.addElement(SharedPrefConstants.CONSUMER_ID, consumerId!!)
    }

    fun getKlarnaItemsRequest(paymentRequest: PaymentRequest): KlarnaItemsRequest? {
        return paymentRequest.klarnaItemsRequest
    }

    override fun getTransactionType(): String? {
        return "wpf_payment"
    }

    private fun isRecurringEnabled(transactionTypesList: List<String>): Boolean {
        return (transactionTypesList.contains(WPFTransactionTypes.INIT_RECURRING_SALE.value)
                || transactionTypesList.contains(WPFTransactionTypes.INIT_RECURRING_SALE3D.value))
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
