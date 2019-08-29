package com.emerchantpay.gateway.genesisandroid

import com.emerchantpay.gateway.genesisandroid.api.models.Transaction
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper

import org.junit.Before
import org.junit.Test

import java.math.BigDecimal

import junit.framework.Assert.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class TransactionUnitTest {

    private var wrapper: NodeWrapper? = null
    private var transaction: Transaction? = null

    @Before
    fun setup() {
        wrapper = mock(NodeWrapper::class.java)
    }

    @Test
    fun testTransaction() {
        `when`(wrapper!!.findString("status")).thenReturn("approved")
        `when`(wrapper!!.findBigDecimal("amount")).thenReturn(BigDecimal("2.00"))

        transaction = Transaction(wrapper)

        assertEquals(transaction!!.status, wrapper!!.findString("status"))
        assertEquals(transaction!!.amount, wrapper!!.findBigDecimal("amount"))
    }
}
