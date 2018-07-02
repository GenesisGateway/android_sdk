package com.emerchantpay.gateway.genesisandroid;

import android.content.Context;
import android.test.mock.MockContext;

import com.emerchantpay.gateway.genesisandroid.api.internal.Genesis;
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.response.Response;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.models.Transaction;
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenesisUnitTest {

    private Context context;
    private Genesis genesisHandler;
    private Configuration configuration;
    private PaymentRequest request;
    private Response response;
    private GenesisError error;
    private GenesisValidator validator;
    private Transaction transaction;

    @Before
    public void setup() {
        context = new MockContext();
        configuration = mock(Configuration.class);
        request = mock(PaymentRequest.class);
        response = mock(Response.class);
        genesisHandler = mock(Genesis.class);
        validator = mock(GenesisValidator.class);
        error = mock(GenesisError.class);
        transaction = mock(Transaction.class);
    }

    @Test
    public void testGenesisError() {
        when(genesisHandler.isValidData()).thenReturn(false);
        when(request.getValidator()).thenReturn(validator);
        when(request.getRequest()).thenReturn(request);

        error = request.getValidator().getError();

        genesisHandler.push();

        assertEquals(genesisHandler.getError(), error);
    }

    @Test
    public void testGenesisRequest() {
        when(genesisHandler.getPaymentRequest()).thenReturn(request);

        assertEquals(genesisHandler.getPaymentRequest(), request);
    }

    @Test
    public void testGenesisResponse() {
        when(genesisHandler.isValidData()).thenReturn(true);
        when(request.getValidator()).thenReturn(validator);
        when(genesisHandler.getResponse()).thenReturn(response);

        genesisHandler.push();

        assertNotNull(genesisHandler.getResponse());
    }

    @Test
    public void testSuccessResponse() {
        genesisHandler = mock(Genesis.class);
        when(genesisHandler.getResponse()).thenReturn(response);
        when(response.getTransaction()).thenReturn(transaction);
        when(response.getStatus()).thenReturn("approved");
        when(response.isSuccess()).thenReturn(true);

        assertTrue(genesisHandler.getResponse().isSuccess());
    }

    @Test
    public void testFailureResponse() {
        genesisHandler = mock(Genesis.class);
        when(genesisHandler.getResponse()).thenReturn(response);
        when(response.getTransaction()).thenReturn(transaction);
        when(response.getStatus()).thenReturn("error");
        when(response.isSuccess()).thenReturn(false);

        assertFalse(genesisHandler.getResponse().isSuccess());
    }
}
