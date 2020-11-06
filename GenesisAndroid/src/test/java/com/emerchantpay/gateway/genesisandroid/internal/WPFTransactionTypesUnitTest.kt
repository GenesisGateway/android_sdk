package com.emerchantpay.gateway.genesisandroid.internal

import com.emerchantpay.gateway.genesisandroid.api.constants.WPFTransactionTypes
import org.junit.Test

import org.junit.Assert.*

class WPFTransactionTypesUnitTest {

    @Test
    fun testToString() {
        assertEquals(TRANSACTION_TYPE_KLARNA_LOWERCASE, WPFTransactionTypes.KLARNA_AUTHORIZE.toString())
    }

    companion object {
        private const val TRANSACTION_TYPE_KLARNA_LOWERCASE = "klarna_authorize"
    }
}