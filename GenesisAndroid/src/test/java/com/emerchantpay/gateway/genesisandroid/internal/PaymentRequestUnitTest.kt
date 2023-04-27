package com.emerchantpay.gateway.genesisandroid.internal

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.constants.ReminderConstants
import com.emerchantpay.gateway.genesisandroid.api.constants.WPFTransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.constants.recurring.RecurringCategory
import com.emerchantpay.gateway.genesisandroid.api.constants.recurring.RecurringType
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.googlepay.definitions.GooglePayPaymentSubtype
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.*
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest
import com.emerchantpay.gateway.genesisandroid.api.models.Country
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress
import com.emerchantpay.gateway.genesisandroid.api.models.Reminder
import com.emerchantpay.gateway.genesisandroid.api.models.threedsv2.ThreeDsV2CardHolderAccountParams
import com.emerchantpay.gateway.genesisandroid.api.models.threedsv2.ThreeDsV2MerchantRiskParams
import com.emerchantpay.gateway.genesisandroid.api.models.threedsv2.ThreeDsV2Params
import com.emerchantpay.gateway.genesisandroid.api.models.threedsv2.ThreeDsV2RecurringParams
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.net.MalformedURLException
import java.text.SimpleDateFormat
import java.util.*


class PaymentRequestUnitTest {
    private var context: Context? = null

    private var paymentRequest: PaymentRequest? = null
    private var address: PaymentAddress? = null

    private var transactionTypes: TransactionTypesRequest? = null

    @Before
    @Throws(IllegalAccessException::class)
    fun mockParams() {
        createPaymentRequest()
    }

    private fun createPaymentRequest(transactionType: WPFTransactionTypes = WPFTransactionTypes.SALE) {
        context = mockk(relaxed = true)

        // Generate unique Id
        val uniqueId = UUID.randomUUID().toString()

        // Address
        address = PaymentAddress("John", "Doe", "Berlin street 1",
            "Berlin street 1", "10000", "Berlin",
            "Berlin state", Country.Germany)

        // Transaction types
        // Create Transaction types
        transactionTypes = TransactionTypesRequest()
        transactionTypes!!.addTransaction(transactionType)

        // Payment paymentRequest
        paymentRequest = PaymentRequest(context!!, uniqueId,
            BigDecimal("2.00"), Currency.USD,
            "john@example.com", "+55555555", address!!,
            "https://example.com", transactionTypes!!)

        paymentRequest?.setConsumerId("123456")

        val threeDsV2Params = ThreeDsV2Params.build {
            purchaseCategory = ThreeDsV2PurchaseCategory.GOODS

            val merchantRiskPreorderDate = SimpleDateFormat("dd-MM-yyyy").calendar.apply {
                time = Date()
                add(Calendar.DATE, 5)
            }.time

            merchantRisk = ThreeDsV2MerchantRiskParams(
                ThreeDsV2MerchantRiskShippingIndicator.DIGITAL_GOODS,
                ThreeDsV2MerchantRiskDeliveryTimeframe.SAME_DAY,
                ThreeDsV2MerchantRiskReorderItemsIndicator.REORDERED,
                ThreeDsV2MerchantRiskPreorderPurchaseIndicator.MERCHANDISE_AVAILABLE,
                merchantRiskPreorderDate,
                true, 3
            )

            cardHolderAccount = ThreeDsV2CardHolderAccountParams(
                SimpleDateFormat("dd-MM-yyyy").parse("11-02-2021"),
                ThreeDsV2CardHolderAccountUpdateIndicator.UPDATE_30_TO_60_DAYS,
                SimpleDateFormat("dd-MM-yyyy").parse("13-02-2021"),
                ThreeDsV2CardHolderAccountPasswordChangeIndicator.PASSWORD_CHANGE_NO_CHANGE,
                SimpleDateFormat("dd-MM-yyyy").parse("10-01-2021"),
                ThreeDsV2CardHolderAccountShippingAddressUsageIndicator.ADDRESS_USAGE_MORE_THAN_60DAYS,
                SimpleDateFormat("dd-MM-yyyy").parse("10-01-2021"),
                2, 129, 1, 31,
                ThreeDsV2CardHolderAccountSuspiciousActivityIndicator.NO_SUSPICIOUS_OBSERVED,
                ThreeDsV2CardHolderAccountRegistrationIndicator.REGISTRATION_30_TO_60_DAYS,
                SimpleDateFormat("dd-MM-yyyy").parse("03-01-2021")
            )

            recurring = ThreeDsV2RecurringParams()
        }

        paymentRequest?.setThreeDsV2Params(threeDsV2Params)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testLoadParams() {
        paymentRequest!!.loadParams()

        assertEquals(PaymentRequest.getCurrency(paymentRequest!!), "USD")
        assertEquals(PaymentRequest.getAmount(paymentRequest!!).toString(), "2.00")
        assertEquals(paymentRequest!!.getCustomerEmail(), "john@example.com")
        assertEquals(paymentRequest!!.getCustomerPhone(), "+55555555")
        assertEquals(paymentRequest!!.getNotificationUrl(), "https://example.com")
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testLoadAddress() {
        address?.let { paymentRequest!!.setBillingAddress(it) }

        assertEquals(paymentRequest!!.paymentAddress?.address1, address!!.address1)
        assertEquals(paymentRequest!!.paymentAddress?.firstName, address!!.firstName)
        assertEquals(paymentRequest!!.paymentAddress?.lastname, address!!.lastname)
        assertEquals(paymentRequest!!.paymentAddress?.countryCode, address!!.countryCode)
        assertEquals(paymentRequest!!.paymentAddress?.countryName, address!!.countryName)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testLoadAddressWithEmptyValues() {
        // Generate unique Id
        val uniqueId = UUID.randomUUID().toString()

        // Address
        address = PaymentAddress("John", "Doe", "",
            "", "", "",
            "Berlin state", Country.Germany)

        paymentRequest = PaymentRequest(context!!, uniqueId,
            BigDecimal("2.00"), Currency.USD,
            "john@example.com", "+55555555", address!!,
            "https://example.com", transactionTypes!!)

        assertTrue(paymentRequest!!.paymentAddress?.address1?.isEmpty() == true)
        assertTrue(paymentRequest!!.paymentAddress?.address2?.isEmpty() == true)
        assertFalse(paymentRequest!!.paymentAddress?.firstName?.isEmpty() == true)
        assertFalse(paymentRequest!!.paymentAddress?.lastname?.isEmpty() == true)
        assertFalse(paymentRequest!!.paymentAddress?.countryCode?.isEmpty() == true)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testLoadTransactionTypes() {
        assertTrue(transactionTypes!!.transactionTypesList.contains("sale"))
    }

    @Test
    fun testNotificationUrl() {
        assertEquals(paymentRequest!!.getNotificationUrl(), "https://example.com")
    }

    @Test
    @Throws(MalformedURLException::class, IllegalAccessException::class)
    fun testEmptyNotificationUrl() {
        // Generate unique Id
        val uniqueId = UUID.randomUUID().toString()

        // Payment paymentRequest
        paymentRequest = context?.let {
            address?.let { it1 ->
                transactionTypes?.let { it2 ->
                    PaymentRequest(it, uniqueId,
                        BigDecimal("2.00"), Currency.USD,
                        "john@example.com", "+55555555", it1,
                        "", it2)
                }
            }
        }

        assertTrue(paymentRequest!!.notificationUrl.isEmpty())
    }

    @Test
    fun isValidRequestData() {
        paymentRequest!!.isValidData?.let { assertTrue(it) }
    }

    @Test
    fun testGetTransactionType() {
        assertEquals(paymentRequest!!.getTransactionType(), "wpf_payment")
    }

    @Test
    fun testPayLater() {
        paymentRequest!!.setPayLater(true)
        paymentRequest!!.getPayLater()?.let { assertTrue(it) }
    }

    @Test
    fun testCrypto() {
        paymentRequest!!.setCrypto(true)
        assertTrue(paymentRequest!!.getCrypto())
    }

    @Test
    fun testGaming() {
        paymentRequest!!.setGaming(true)
        assertTrue(paymentRequest!!.getGaming())
    }

    @Test
    fun testRememberCard() {
        paymentRequest!!.setRememberCard(true)
        assertTrue(paymentRequest!!.getRememberCard())
    }

    @Test
    fun testRemindersSuccess() {
        paymentRequest!!.setPayLater(true)
            .addReminder(ReminderConstants.REMINDERS_CHANNEL_EMAIL, 1)
            .addReminder(ReminderConstants.REMINDERS_CHANNEL_SMS, 1).done()

        assertNotNull(paymentRequest!!.reminders)
    }

    @Test
    fun testRemindersFailure() {
        paymentRequest!!.setPayLater(true)
            .addReminder("test", 1).done()
        assertEquals(paymentRequest!!.reminders.remindersList, ArrayList<Reminder>())
    }

    @Test
    fun testBuildRequest() {
        assertEquals(paymentRequest!!.toXML(), paymentRequest!!.request.toXML())
    }

    @Test
    fun testWithoutRecurringParams() {
        mockParams()
        paymentRequest!!.isValidData?.let { assertTrue(it) }
    }

    @Test
    fun testRecurringParams() {
        paymentRequest?.setRecurringType(RecurringType.INITIAL)
        paymentRequest?.setRecurringCategory(RecurringCategory.SUBSCRIPTION)
        paymentRequest!!.isValidData?.let { assertTrue(it) }
    }

    @Test
    fun testValidGooglePayTransactionRequest() {
        createPaymentRequest(WPFTransactionTypes.GOOGLE_PAY)
        paymentRequest?.setGooglePayPaymentSubtype(GooglePayPaymentSubtype.AUTHORIZE)

        paymentRequest?.transactionTypes?.transactionTypesList?.any { it == WPFTransactionTypes.GOOGLE_PAY.value }
            ?.let { assertTrue(it) }

        paymentRequest?.toXML()?.let { it.run {
            assertTrue(contains("payment_subtype"))
            assertFalse(contains("threeds_v2_params"))
        } }
    }

    @Test
    fun testInvvalidGooglePayTransactionRequest() {
        createPaymentRequest(WPFTransactionTypes.GOOGLE_PAY)

        paymentRequest?.transactionTypes?.transactionTypesList?.any { it == WPFTransactionTypes.GOOGLE_PAY.value }
            ?.let { assertTrue(it) }

        paymentRequest?.isValidData?.let { assertFalse(it) }
        paymentRequest?.toXML()?.let { it.run {
            assertFalse(contains("payment_subtype"))
        } }
    }
}