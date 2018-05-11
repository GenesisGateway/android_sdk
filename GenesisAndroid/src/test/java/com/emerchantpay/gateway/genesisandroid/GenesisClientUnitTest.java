package com.emerchantpay.gateway.genesisandroid;

import com.emerchantpay.gateway.genesisandroid.api.exceptions.AuthenticationException;
import com.emerchantpay.gateway.genesisandroid.api.internal.GenesisClient;
import com.emerchantpay.gateway.genesisandroid.api.internal.TransactionGateway;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper;
import com.emerchantpay.gateway.genesisandroid.api.util.Request;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class GenesisClientUnitTest {

    private Configuration configuration;
    private GenesisClient client;
    private PaymentRequest request;
    private TransactionGateway gateway;
    private NodeWrapper response;

    @Before
    public void setup() {
        configuration = mock(Configuration.class);
        request = mock(PaymentRequest.class);
        client = mock(GenesisClient.class);
        gateway = mock(TransactionGateway.class);
        response = mock(NodeWrapper.class);
    }

    @Test
    public void executeRequest() {

        when(client.debugMode(isA(Boolean.class))).thenReturn(client);
        when(client.changeRequest(isA(Request.class))).thenReturn(client);
        when(client.getRequest()).thenReturn(request);
        when(client.getTransactionType()).thenCallRealMethod();
        when(client.getResponse()).thenReturn(response);
        when(client.getTransaction()).thenReturn(gateway);
        when(client.execute()).thenReturn(request);

        assertEquals(client.debugMode(true), client);
        assertEquals(client.changeRequest(request), client);
        assertEquals(client.getRequest(), request);
        assertEquals(client.getTransactionType(), request.getTransactionType());
        assertEquals(client.getResponse(), response);
        assertEquals(client.getTransaction(), gateway);
        assertEquals(client.execute(), request);

        verify(client).debugMode(true);
        verify(client).changeRequest(request);
        verify(client).getRequest();
        verify(client).getTransactionType();
        verify(client).getResponse();
        verify(client).getTransaction();
        verify(client).execute();

        verifyNoMoreInteractions(client);
    }

    @Test(expected = AuthenticationException.class)
    public void executeRequestWithWrongCredentials() {

        when(client.debugMode(isA(Boolean.class))).thenReturn(client);
        when(client.changeRequest(isA(Request.class))).thenReturn(client);
        when(client.execute()).thenThrow(AuthenticationException.class);

        assertEquals(client.debugMode(true), client);
        assertEquals(client.changeRequest(request), client);
        assertEquals(client.execute(), request);


        verify(client).debugMode(true);
        verify(client).changeRequest(request);
        verify(client).execute();

        verifyNoMoreInteractions(client);
    }
}
