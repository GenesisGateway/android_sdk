package com.emerchantpay.gateway.genesisandroid.internal

import com.emerchantpay.gateway.genesisandroid.api.constants.WPFTransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TransactionTypesRequestUnitTest {

    private lateinit var transactionTypesRequest: TransactionTypesRequest

    @Before
    fun setUp() {
        transactionTypesRequest = TransactionTypesRequest()
    }

    @Test
    fun testAddTransactionAsStringParam() {
        transactionTypesRequest.addTransaction(TRANSACTION_TYPE_1)
            .addTransaction(TRANSACTION_TYPE_2)
            .addTransaction(TRANSACTION_TYPE_3)

        assertEquals(listOf(TRANSACTION_TYPE_1, TRANSACTION_TYPE_2, TRANSACTION_TYPE_3), transactionTypesRequest.transactionTypesList)
    }

    @Test
    fun testAddTransactionsAsStringParams() {
        transactionTypesRequest.addTransactions(TRANSACTION_TYPE_1, TRANSACTION_TYPE_2, TRANSACTION_TYPE_3)

        assertEquals(listOf(TRANSACTION_TYPE_1, TRANSACTION_TYPE_2, TRANSACTION_TYPE_3), transactionTypesRequest.transactionTypesList)
    }

    @Test
    fun testAddTransactionAsEnumParam() {
        transactionTypesRequest.addTransaction(WPFTransactionTypes.SALE3D)
            .addTransaction(WPFTransactionTypes.UPI)
            .addTransaction(WPFTransactionTypes.ONLINE_BANKING)

        assertEquals(listOf(TRANSACTION_TYPE_1, TRANSACTION_TYPE_2, TRANSACTION_TYPE_3), transactionTypesRequest.transactionTypesList)
    }

    @Test
    fun testAddTransactionsAsEnumParams() {
        transactionTypesRequest.addTransactions(WPFTransactionTypes.SALE3D, WPFTransactionTypes.UPI, WPFTransactionTypes.ONLINE_BANKING)

        assertEquals(listOf(TRANSACTION_TYPE_1, TRANSACTION_TYPE_2, TRANSACTION_TYPE_3), transactionTypesRequest.transactionTypesList)
    }

    @Test
    fun testConvertToXml() {
        transactionTypesRequest.addTransactions(WPFTransactionTypes.SALE3D, WPFTransactionTypes.UPI, WPFTransactionTypes.ONLINE_BANKING)

        assertEquals(transactionTypesRequest.toXML(), transactionTypesRequest.request.toXML())
    }

    companion object {
        private const val TRANSACTION_TYPE_1 = "sale3d"
        private const val TRANSACTION_TYPE_2 = "upi"
        private const val TRANSACTION_TYPE_3 = "online_banking"
    }
}