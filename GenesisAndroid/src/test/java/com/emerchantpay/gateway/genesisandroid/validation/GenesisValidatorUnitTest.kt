package com.emerchantpay.gateway.genesisandroid.validation

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages
import com.emerchantpay.gateway.genesisandroid.api.constants.ReminderConstants
import com.emerchantpay.gateway.genesisandroid.api.constants.WPFTransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.constants.recurring.RecurringCategory
import com.emerchantpay.gateway.genesisandroid.api.constants.recurring.RecurringType
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.googlepay.definitions.GooglePayPaymentSubtype
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.models.*
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class GenesisValidatorUnitTest {

    // Application context
    private var context: Context? = null

    // Genesis Validator
    private var validator: GenesisValidator? = null

    // Genesis Error handler
    private var error: GenesisError? = null

    // Payment request
    private var request: PaymentRequest? = null

    // Payment address
    private var billingAddress: PaymentAddress? = null

    // Transaction types
    private var transactionTypes: TransactionTypesRequest? = null

    // Parameters
    private var transactionId: String? = null
    private var amount: BigDecimal? = null
    private var customerEmail: String? = null
    private var customerPhone: String? = null
    private var notificationUrl: String? = null
    private var testParam: String? = null

    @Before
    @Throws(IllegalAccessException::class)
    fun setup() {
        createPaymentRequest(listOf(WPFTransactionTypes.AUTHORIZE, WPFTransactionTypes.EZEEWALLET))
    }

    private fun createPaymentRequest(transactions: List<WPFTransactionTypes>) {
        context = mockk(relaxed = true)
        validator = GenesisValidator()

        // Intitial params
        transactionId = UUID.randomUUID().toString()
        amount = BigDecimal("2.00")
        customerEmail = "johndoe@example.com"
        customerPhone = "+555555555"
        notificationUrl = "http://google.com"

        // Test param
        testParam = "test_param"

        // Address
        billingAddress = PaymentAddress("John", "Doe",
            "address1", "", "10000", "New York",
            "state", Country().getCountry("United States")!!)

        // Transaction types list
        transactionTypes = TransactionTypesRequest()
        transactions.forEach { transactionTypes!!.addTransaction(it) }

        // Payment request
        request = context?.let {
            PaymentRequest(it, transactionId!!, amount!!, Currency.USD,
                customerEmail!!, customerPhone!!, billingAddress!!, notificationUrl, transactionTypes!!)
        }
    }

    // Transaction Id
    @Test
    fun testTransactionId() {
        assertTrue(validator!!.validateString("transaction_id", transactionId)!!)

        // If Transaction Id is empty
        transactionId = ""
        assertFalse(validator!!.validateString("transaction_id", transactionId)!!)
    }

    // Amount
    @Test
    fun testAmountValidationSuccess() {
        assertTrue(validator!!.validateAmount(amount)!!)

        amount = BigDecimal("0.99")
        assertTrue(validator!!.validateAmount(amount)!!)
    }

    @Test
    fun testAmountValidationFailed() {
        amount = BigDecimal("0.00")
        assertFalse(validator!!.validateAmount(amount)!!)

        error = GenesisError(ErrorMessages.INVALID_AMOUNT)
        assertEquals(error!!.message, ErrorMessages.INVALID_AMOUNT)
    }

    // Empty params
    @Test
    fun testStringValidationSuccess() {
        assertTrue(validator!!.validateString("test_param", testParam)!!)
    }

    @Test
    fun testStringValidationFailed() {
        testParam = ""
        assertFalse(validator!!.validateString("test_param", testParam)!!)

        error = GenesisError(ErrorMessages.EMPTY_PARAM, testParam!!)
        assertEquals(error!!.message, ErrorMessages.EMPTY_PARAM)
        assertEquals(error!!.technicalMessage, testParam)
    }

    // Email
    @Test
    fun testEmailValidationSuccess() {
        assertTrue(validator!!.validateEmail(customerEmail)!!)
    }

    @Test
    fun testEmailValidationFailed() {
        customerEmail = "johndoe.example.com"
        assertFalse(validator!!.validateEmail(customerEmail)!!)

        error = GenesisError(ErrorMessages.INVALID_EMAIL, customerEmail!!)
        assertEquals(error!!.message, ErrorMessages.INVALID_EMAIL)
        assertEquals(error!!.technicalMessage, customerEmail)
    }

    // Phone
    @Test
    fun testPhoneValidationSuccess() {
        assertTrue(validator!!.validatePhone(customerPhone)!!)
    }

    @Test
    fun testPhoneValidationFailed() {
        customerPhone = "test555555555"
        assertFalse(validator!!.validatePhone(customerPhone)!!)

        error = GenesisError(ErrorMessages.INVALID_PHONE, customerPhone!!)
        assertEquals(error!!.message, ErrorMessages.INVALID_PHONE)
        assertEquals(error!!.technicalMessage, customerPhone)
    }

    // URLs
    @Test
    fun testUrlValidationSuccess() {
        assertTrue(validator!!.validateNotificationUrl(notificationUrl)!!)
    }

    @Test
    fun testUrlValidationFailed() {
        notificationUrl = ":// should fail"
        assertFalse(validator!!.validateNotificationUrl(notificationUrl)!!)

        notificationUrl = "."
        assertFalse(validator!!.validateNotificationUrl(notificationUrl)!!)

        notificationUrl = "google.com"
        assertFalse(validator!!.validateNotificationUrl(notificationUrl)!!)

        error = GenesisError(ErrorMessages.INVALID_NOTIFICATION_URL, notificationUrl!!)
        assertEquals(error!!.message, ErrorMessages.INVALID_NOTIFICATION_URL)
        assertEquals(error!!.technicalMessage, notificationUrl)
    }

    @Test
    fun testIsRequestValid() {
        assertTrue(request?.let { validator!!.isValidRequest(it) }!!)
    }

    @Test
    fun testIsAddressValid() {
        assertTrue(billingAddress?.let { validator!!.isValidAddress(it) }!!)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testTransactionTypeValidionSuccess() {
        assertTrue(validator!!.validateTransactionType(WPFTransactionTypes.AUTHORIZE.value)!!)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testTransactionTypeValidionFailed() {
        assertFalse(validator!!.validateTransactionType("test_transaction_type")!!)
    }

    @Test
    fun testRemindersSuccess() {
        assertTrue(validator!!.validateReminder(ReminderConstants.REMINDERS_CHANNEL_EMAIL, 1)!!)
        assertTrue(validator!!.validateReminder(ReminderConstants.REMINDERS_CHANNEL_SMS, 1)!!)
    }

    @Test
    fun testRemindersFailure() {
        assertFalse(validator!!.validateReminder("test", 1)!!)
    }

    @Test
    fun testRemindersNumberSuccess() {
        assertTrue(validator!!.validateRemindersNumber(addReminders(3))!!)
    }

    @Test
    fun testRemindersNumberFailure() {
        assertFalse(validator!!.validateRemindersNumber(addReminders(4))!!)
    }

    @Test
    fun testConsumerIdSuccess() {
        assertTrue(validator!!.isValidConsumerId("123456")!!)
        assertTrue(validator!!.isValidConsumerId("9999999999")!!)
    }

    @Test
    fun testConsumerIdFailure() {
        assertFalse(validator!!.isValidConsumerId("12345678901")!!)
        assertFalse(validator!!.isValidConsumerId("test123")!!)
    }

    private fun addReminders(reminderNumber: Int?): ArrayList<Reminder> {
        val remindersList = ArrayList<Reminder>()

        for (i in 0 until reminderNumber!!) {
            val reminder = Reminder(ReminderConstants.REMINDERS_CHANNEL_EMAIL, 1)
            remindersList.add(reminder)
        }

        return remindersList
    }

    @Test
    fun testIsValidData() {
        assertTrue(request?.let { validator!!.isValidRequest(it) }!!)
    }

    @Test
    fun testValidDataWithoutRecurring() {
        setup()
        assertTrue(request?.let { validator!!.isValidRequest(it) }!!)
    }

    @Test
    fun testValidDataWithRecurring() {
        request?.setRecurringType(RecurringType.INITIAL)
        request?.setRecurringCategory(RecurringCategory.SUBSCRIPTION)
        assertTrue(request?.let { validator!!.isValidRequest(it) }!!)
    }

    @Test
    fun testValidDataWithGooglePayTransaction() {
        createPaymentRequest(listOf(WPFTransactionTypes.GOOGLE_PAY))

        request?.setGooglePayPaymentSubtype(GooglePayPaymentSubtype.INIT_RECURRING_SALE)
        assertTrue(request?.let { validator!!.isValidRequest(it) }!!)
    }
}
