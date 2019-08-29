package com.emerchantpay.gateway.genesisandroid.internal

import com.emerchantpay.gateway.genesisandroid.api.internal.response.Response
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.models.Transaction
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ResponseUnitTest {
    private var response: Response? = null
    private var transaction: Transaction? = null
    private var error: GenesisError? = null

    @Before
    fun setup() {
        response = mock(Response::class.java)
        transaction = mock(Transaction::class.java)
    }

    @Test
    fun testFailedResponse() {
        `when`(response!!.transaction).thenReturn(transaction)
        `when`(response!!.status).thenReturn("error")

        // Init Genesis error
        error = response!!.status?.let { GenesisError(it) }

        `when`(response!!.isSuccess).thenReturn(false)
        `when`(response!!.error).thenReturn(error)

        assertFalse(response!!.isSuccess!!)
        assertEquals(response!!.error!!.message, "error")
    }

    @Test
    fun testSuccessResponse() {
        `when`(response!!.transaction).thenReturn(transaction)
        `when`(response!!.status).thenReturn("approved")

        // Init Genesis error
        error = response!!.status?.let { GenesisError(it) }

        `when`(response!!.isSuccess).thenReturn(true)
        `when`(response!!.error).thenReturn(error)

        assertTrue(response!!.isSuccess!!)
        assertEquals(response!!.error!!.message, "approved")
    }
}
