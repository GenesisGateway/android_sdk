package com.emerchantpay.gateway.genesisandroid.internal

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.internal.request.RetrieveConsumerRequest
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.MalformedURLException

class RetrieveConsumerRequestUnitTest {

    private var context: Context? = null

    private var request: RetrieveConsumerRequest? = null

    @Before
    @Throws(IllegalAccessException::class, MalformedURLException::class)
    fun mockParams() {
        context = mockk<Context>(relaxed = true)

        // Retrieve Consumer request
        request = context?.let { RetrieveConsumerRequest(it, "test@example.com") }
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testRetrieveConsumer() {
        assertEquals(request!!.getTransactionType(), "retrieve_consumer_request")
    }

    @Test
    fun testBuildRequest() {
        assertEquals(request!!.toXML(), request!!.request.toXML())
    }
}
