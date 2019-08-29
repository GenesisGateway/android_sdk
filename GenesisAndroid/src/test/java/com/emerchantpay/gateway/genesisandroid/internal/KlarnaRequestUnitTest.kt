package com.emerchantpay.gateway.genesisandroid.internal

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.constants.KlarnaItemTypes
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator
import com.emerchantpay.gateway.genesisandroid.api.models.*
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.models.klarna.KlarnaItem
import io.mockk.mockk
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class KlarnaRequestUnitTest {

    // Application context
    private var context: Context? = null

    // Genesis Validator
    private var validator: GenesisValidator? = null

    // Genesis Error handler
    private val error: GenesisError? = null

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

    @Before
    @Throws(IllegalAccessException::class)
    fun setupOneItem() {
        context = mockk<Context>(relaxed = true)

        validator = GenesisValidator()

        // Intitial params
        transactionId = UUID.randomUUID().toString()
        amount = BigDecimal("4.00")
        customerEmail = "johndoe@example.com"
        customerPhone = "+555555555"
        notificationUrl = "http://google.com"

        // Address
        billingAddress = PaymentAddress("John", "Doe",
                "address1", "", "10000", "New York",
                "state", Country().getCountry("United States")!!)

        // Transaction types list
        transactionTypes = TransactionTypesRequest()
        transactionTypes!!.addTransaction(WPFTransactionTypes.klarnaAuthorize)

        // Payment request
        request = context?.let {
            PaymentRequest(it, transactionId!!, amount!!, Currency.USD,
                    customerEmail!!, customerPhone!!, billingAddress!!, notificationUrl, transactionTypes!!)
        }
    }

    @Test
    fun testWithOneItem() {
        val klarnaItem = KlarnaItem("TICKETS", KlarnaItemTypes.PHYSICAL, 2,
                BigDecimal("2.00"), BigDecimal("4.00"))
        request!!.addKlarnaItem(klarnaItem)
        assertTrue(amount?.let { validator!!.isValidKlarnaRequest(request!!.klarnaItemsRequest, it, request!!.orderTaxAmount) }!!)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testWithMoreThanOneItem() {
        amount = BigDecimal("6.00")

        // Payment request
        request = context?.let {
            transactionId?.let { it1 ->
                customerEmail?.let { it2 ->
                    customerPhone?.let { it3 ->
                        billingAddress?.let { it4 ->
                            transactionTypes?.let { it5 ->
                                PaymentRequest(it, it1, amount!!, Currency.USD,
                                        it2, it3, it4, notificationUrl, it5)
                            }
                        }
                    }
                }
            }
        }

        // Klarna Item 1
        val item1 = KlarnaItem("TICKETS", KlarnaItemTypes.PHYSICAL, 2,
                BigDecimal("2.00"), BigDecimal("4.00"))

        // Klarna Item 2
        val item2 = KlarnaItem("TICKETS", KlarnaItemTypes.DISCOUNT, 2,
                BigDecimal("2.00"), BigDecimal("2.00"))
        item2.setTotalDiscountAmount(BigDecimal("2.00"))

        val klarnaItemsList = ArrayList<KlarnaItem>()
        klarnaItemsList.add(item1)
        klarnaItemsList.add(item2)

        request!!.addKlarnaItems(klarnaItemsList)

        assertTrue(validator!!.isValidKlarnaRequest(request!!.klarnaItemsRequest, amount!!, request!!.orderTaxAmount)!!)
    }

    @Test
    fun testWithoutItems() {
        assertFalse(amount?.let { validator!!.isValidKlarnaRequest(request!!.klarnaItemsRequest, it, request!!.orderTaxAmount) }!!)
    }
}
