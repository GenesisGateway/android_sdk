package com.emerchantpay.gateway.genesisandroid

import com.emerchantpay.gateway.genesisandroid.api.exceptions.AuthenticationException
import com.emerchantpay.gateway.genesisandroid.api.internal.GenesisClient
import com.emerchantpay.gateway.genesisandroid.api.internal.TransactionGateway
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper
import com.emerchantpay.gateway.genesisandroid.api.util.Request
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.isA
import org.mockito.Mockito.*

class GenesisClientUnitTest {

    private var configuration: Configuration? = null
    private var client: GenesisClient? = null
    private var request: PaymentRequest? = null
    private var gateway: TransactionGateway? = null
    private var response: NodeWrapper? = null

    @Before
    fun setup() {
        configuration = mock(Configuration::class.java)
        request = mock(PaymentRequest::class.java)
        client = mock(GenesisClient::class.java)
        gateway = mock(TransactionGateway::class.java)
        response = mock(NodeWrapper::class.java)
    }

    @Test
    fun executeRequest() {

        `when`(client!!.debugMode(isA(Boolean::class.java))).thenReturn(client)
        `when`(client!!.changeRequest(isA(Request::class.java))).thenReturn(client)
        `when`(client!!.request).thenReturn(request)
        `when`(client!!.getTransactionType()).thenCallRealMethod()
        `when`(client!!.response).thenReturn(response)
        `when`(client!!.transaction).thenReturn(gateway)
        `when`(client!!.execute()).thenReturn(request)

        assertEquals(client!!.debugMode(true), client)
        assertEquals(request?.let { client!!.changeRequest(it) }, client)
        assertEquals(client!!.request, request)
        assertEquals(client!!.getTransactionType(), request!!.getTransactionType())
        assertEquals(client!!.response, response)
        assertEquals(client!!.transaction, gateway)
        assertEquals(client!!.execute(), request)

        verify(client)!!.debugMode(true)
        verify(client)!!.changeRequest(request!!)
        verify(client)!!.request
        verify(client)!!.getTransactionType()
        verify(client)!!.response
        verify(client)!!.transaction
        verify(client)!!.execute()

        verifyNoMoreInteractions(client)
    }

    @Test(expected = AuthenticationException::class)
    fun executeRequestWithWrongCredentials() {

        `when`(client!!.debugMode(isA(Boolean::class.java))).thenReturn(client)
        `when`(client!!.changeRequest(isA(Request::class.java))).thenReturn(client)
        `when`(client!!.execute()).thenThrow(AuthenticationException::class.java)

        assertEquals(client!!.debugMode(true), client)
        assertEquals(request?.let { client!!.changeRequest(it) }, client)
        assertEquals(client!!.execute(), request)


        verify(client)!!.debugMode(true)
        request?.let { verify(client)!!.changeRequest(it) }
        verify(client)!!.execute()

        verifyNoMoreInteractions(client)
    }
}
