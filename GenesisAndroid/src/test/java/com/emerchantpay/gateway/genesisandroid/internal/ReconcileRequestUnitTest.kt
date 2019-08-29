package com.emerchantpay.gateway.genesisandroid.internal

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.internal.request.ReconcileRequest
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import java.net.MalformedURLException
import java.util.*

class ReconcileRequestUnitTest {

    @Mock
    private val context: Context? = null

    private var request: ReconcileRequest? = null
    private val address: PaymentAddress? = null

    private val transactionTypes: ArrayList<String>? = null

    @Before
    @Throws(IllegalAccessException::class, MalformedURLException::class)
    fun mockParams() {
        // Generate unique Id
        val uniqueId = UUID.randomUUID().toString()

        // Payment request
        request = ReconcileRequest()
        request!!.setUniqueId(uniqueId)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testReconcile() {
        assertEquals(request!!.getTransactionType(), "wpf_reconcile")
    }

    @Test
    fun testBuildRequest() {
        assertEquals(request!!.toXML(), request!!.request.toXML())
    }
}
