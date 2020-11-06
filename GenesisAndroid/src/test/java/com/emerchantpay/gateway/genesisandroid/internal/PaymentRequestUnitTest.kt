package com.emerchantpay.gateway.genesisandroid.internal

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.constants.ReminderConstants
import com.emerchantpay.gateway.genesisandroid.api.constants.WPFTransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest
import com.emerchantpay.gateway.genesisandroid.api.models.*
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.net.MalformedURLException
import java.util.*


class PaymentRequestUnitTest {

    private var context: Context? = null

    private var paymentRequest: PaymentRequest? = null
    private var address: PaymentAddress? = null

    private var transactionTypes: TransactionTypesRequest? = null

    @Before
    @Throws(IllegalAccessException::class)
    fun mockParams() {
        context = mockk<Context>(relaxed = true)

        // Generate unique Id
        val uniqueId = UUID.randomUUID().toString()

        // Address
        address = PaymentAddress("John", "Doe", "Berlin street 1",
                "Berlin street 1", "10000", "Berlin",
                "Berlin state", Country.Germany)

        // Transaction types
        // Create Transaction types
        transactionTypes = TransactionTypesRequest()
        transactionTypes!!.addTransaction(WPFTransactionTypes.SALE)

        // Payment paymentRequest
        paymentRequest = PaymentRequest(context!!, uniqueId,
                BigDecimal("2.00"), Currency.USD,
                "john@example.com", "+55555555", address!!,
                "https://example.com", transactionTypes!!)

        paymentRequest?.setConsumerId("123456")
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
}
