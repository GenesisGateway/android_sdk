package com.emerchantpay.gateway.genesisandroid

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.internal.Genesis
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.response.Response
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.models.Transaction
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import java.net.MalformedURLException
import java.net.URL

class GenesisUnitTest {
    @Mock
    private val context: Context? = null

    private var genesisHandler: Genesis? = null
    private var configuration: Configuration? = null
    private var request: PaymentRequest? = null
    private var response: Response? = null
    private var error: GenesisError? = null
    private var validator: GenesisValidator? = null
    private var transaction: Transaction? = null

    @Before
    fun setup() {
        configuration = mock(Configuration::class.java)
        request = mock(PaymentRequest::class.java)
        response = mock(Response::class.java)
        genesisHandler = mock(Genesis::class.java)
        validator = mock(GenesisValidator::class.java)
        error = mock(GenesisError::class.java)
        transaction = mock(Transaction::class.java)
    }

    @Test
    fun testGenesisError() {
        `when`(genesisHandler!!.isValidData).thenReturn(false)
        `when`(request!!.validator).thenReturn(validator)
        `when`(request!!.request).thenReturn(request)

        error = request!!.validator.error

        genesisHandler!!.push()

        assertEquals(genesisHandler!!.error, error)
    }

    @Test
    fun testGenesisRequest() {
        `when`(genesisHandler!!.paymentRequest).thenReturn(request)

        assertEquals(genesisHandler!!.paymentRequest, request)
    }

    @Test
    fun testGenesisResponse() {
        `when`(genesisHandler!!.isValidData).thenReturn(true)
        `when`(request!!.validator).thenReturn(validator)
        `when`(genesisHandler!!.response).thenReturn(response)

        genesisHandler!!.push()

        assertNotNull(genesisHandler!!.response)
    }

    @Test
    fun testSuccessResponse() {
        genesisHandler = mock(Genesis::class.java)
        `when`(genesisHandler!!.response).thenReturn(response)
        `when`(response!!.transaction).thenReturn(transaction)
        `when`(response!!.status).thenReturn("approved")
        `when`(response!!.isSuccess).thenReturn(true)

        assertTrue(genesisHandler!!.response?.isSuccess!!)
    }

    @Test
    fun testFailureResponse() {
        genesisHandler = mock(Genesis::class.java)
        `when`(genesisHandler!!.response).thenReturn(response)
        `when`(response!!.transaction).thenReturn(transaction)
        `when`(response!!.status).thenReturn("error")
        `when`(response!!.isSuccess).thenReturn(false)

        assertFalse(genesisHandler!!.response?.isSuccess!!)
    }

    @Test
    @Throws(MalformedURLException::class)
    fun testSuccessLoadUrl() {
        genesisHandler!!.loadRedirectUrl(URL("https://google.com"))
        verify(genesisHandler, times(1))?.loadRedirectUrl(URL("https://google.com"))
    }

    @Test(expected = Exception::class)
    @Throws(MalformedURLException::class)
    fun testFailureLoadUrl() {
        doThrow().`when`(genesisHandler)?.loadRedirectUrl(null)
        genesisHandler!!.loadRedirectUrl(null)
    }
}
