package com.emerchantpay.gateway.genesisandroid.validation

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.constants.KlarnaItemTypes
import com.emerchantpay.gateway.genesisandroid.api.constants.WPFTransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.RequiredParameters
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.RequiredParametersValidator
import com.emerchantpay.gateway.genesisandroid.api.models.Country
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress
import com.emerchantpay.gateway.genesisandroid.api.models.klarna.KlarnaItem
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class RequiredParametersValidatorUnitTest {

    // Application context
    private var context: Context? = null

    // Genesis Validator
    private val requiredParameters = RequiredParameters()
    private var validator: RequiredParametersValidator? = null

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

    private var requiredParamsMap: HashMap<String, String>? = null

    @Before
    @Throws(IllegalAccessException::class)
    fun setupValidData() {
        // Intitial params
        context = mockk<Context>(relaxed = true)
        transactionId = UUID.randomUUID().toString()
        amount = BigDecimal("2.00")
        customerEmail = "johndoe@example.com"
        customerPhone = "+555555555"
        notificationUrl = "http://google.com"

        // Address
        billingAddress = PaymentAddress("John", "Doe",
                "address1", "", "10000", "New York",
                "state", Country().getCountry("United States")!!)

        // Transaction types list
        transactionTypes = TransactionTypesRequest()
        transactionTypes!!.addTransaction(WPFTransactionTypes.AUTHORIZE)
        transactionTypes!!.addTransaction(WPFTransactionTypes.EZEEWALLET)

        // Payment request
        request = context?.let {
            PaymentRequest(it, transactionId!!, amount!!, Currency.USD,
                    customerEmail!!, customerPhone!!, billingAddress!!, notificationUrl, transactionTypes!!)
        }

        request!!.consumerId = "123456"
    }


    // Request params
    @Test
    fun testRequestParams() {
        val map = request?.let { requiredParameters.getRequiredParametersForRequest(it) }

        validator = RequiredParametersValidator(map)
        assertTrue(validator!!.isValidRequiredParams!!)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testWithMissingRequestParams() {
        // Intitial params
        transactionId = UUID.randomUUID().toString()
        amount = BigDecimal("2.00")
        customerEmail = ""
        customerPhone = ""
        notificationUrl = "http://google.com"

        // Payment request
        request = context?.let {
            billingAddress?.let { it1 ->
                transactionTypes?.let { it2 ->
                    PaymentRequest(it, transactionId, amount, Currency.USD,
                            customerEmail, customerPhone, it1, notificationUrl, it2)
                }
            }
        }

        requiredParamsMap = request?.let { requiredParameters.getRequiredParametersForRequest(it) }

        validator = RequiredParametersValidator(requiredParamsMap)
        assertTrue(validator!!.isValidRequiredParams!!)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testWithNullRequestParams() {
        // Intitial params
        transactionId = UUID.randomUUID().toString()
        amount = BigDecimal("2.00")
        notificationUrl = "http://google.com"

        // Payment request
        request = context?.let {
            billingAddress?.let { it1 ->
                transactionTypes?.let { it2 ->
                    PaymentRequest(it, transactionId, amount, Currency.USD,
                        null, null, it1, notificationUrl, it2)
                }
            }
        }

        requiredParamsMap = request?.let { requiredParameters.getRequiredParametersForRequest(it) }

        validator = RequiredParametersValidator(requiredParamsMap)
        assertTrue(validator!!.isValidRequiredParams!!)
    }

    // Billing address params
    @Test
    fun testAddressParams() {
        val map = billingAddress?.let { requiredParameters.getRequiredParametersForAddress(it) }

        validator = RequiredParametersValidator(map)
        assertTrue(validator!!.isValidRequiredParams!!)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testWithMissingAddressParams() {
        // Address
        billingAddress = PaymentAddress("John", "Doe",
                "", "", "", "",
                "", Country().getCountry("United States")!!)

        requiredParamsMap = requiredParameters.getRequiredParametersForAddress(billingAddress!!)

        validator = RequiredParametersValidator(requiredParamsMap)
        assertFalse(validator!!.isValidRequiredParams!!)
    }

    // Transaction types params
    @Test
    @Throws(IllegalAccessException::class)
    fun testWithTransactionTypesParams() {
        // Transaction types list
        transactionTypes = TransactionTypesRequest()
        transactionTypes!!.addTransaction(WPFTransactionTypes.PPRO)
        transactionTypes!!.addParam("product_name", "TICKETS")

        // Payment request
        request = billingAddress?.let {
            context?.let { it1 ->
                transactionId?.let { it2 ->
                    amount?.let { it3 ->
                        customerEmail?.let { it4 ->
                            customerPhone?.let { it5 ->
                                PaymentRequest(it1, it2, it3, Currency.USD,
                                        it4, it5, it, notificationUrl, transactionTypes!!)
                            }
                        }
                    }
                }
            }
        }

        for (transactionType in transactionTypes!!.transactionTypesList) {
            val map = request?.let { requiredParameters.getRequiredParametersForTransactionType(it, transactionType) }

            validator = RequiredParametersValidator(map)

            assertTrue(validator!!.isValidRequiredParams!!)
        }
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testWithMissingTransactionTypesParams() {
        // Transaction types list
        transactionTypes = TransactionTypesRequest()
        transactionTypes!!.addTransaction(WPFTransactionTypes.PPRO)

        // Payment request
        request = PaymentRequest(context, transactionId, amount, Currency.USD,
                customerEmail, customerPhone, billingAddress, notificationUrl, transactionTypes!!)

        var map: HashMap<String, String>

        transactionTypes!!.transactionTypesList.forEach { transactionType ->
            map = requiredParameters.getRequiredParametersForTransactionType(request!!, transactionType)

            validator = RequiredParametersValidator(map)

            assertFalse(validator!!.isValidRequiredParams!!)
        }
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testTokenization() {
        // Transaction types list
        transactionTypes = TransactionTypesRequest()
        transactionTypes!!.addTransaction(WPFTransactionTypes.PPRO)

        // Payment request
        request = PaymentRequest(context, transactionId, amount, Currency.USD,
                customerEmail, customerPhone, billingAddress, notificationUrl, transactionTypes!!)

        request!!.rememberCard = true

        var map: HashMap<String, String>

        transactionTypes!!.transactionTypesList.forEach { transactionType ->
            map = requiredParameters.getRequiredParametersForTransactionType(request!!, transactionType)

            validator = RequiredParametersValidator(map)

            assertFalse(validator!!.isValidRequiredParams!!)
        }
    }

    // Klarna params
    @Test
    fun testWithKlarnaItems() {
        val item = KlarnaItem("TICKET", KlarnaItemTypes.DISCOUNT, 10,
                BigDecimal(10.00), BigDecimal(2.00))

        request!!.addKlarnaItem(item)

        requiredParamsMap = request!!.klarnaItemsRequest?.let { requiredParameters.getRequiredParametersForKlarnaItem(it) }

        validator = RequiredParametersValidator(requiredParamsMap)
        assertTrue(validator!!.isValidRequiredParams!!)
    }

    @Test
    fun testWithMissingKlarnaItems() {
        val item = KlarnaItem("", KlarnaItemTypes.DISCOUNT, 10,
                BigDecimal(10.00), BigDecimal(2.00))

        request!!.addKlarnaItem(item)

        val map = request!!.klarnaItemsRequest?.let { requiredParameters.getRequiredParametersForKlarnaItem(it) }

        validator = RequiredParametersValidator(map)
        assertFalse(validator!!.isValidRequiredParams!!)
    }
}