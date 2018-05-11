package com.emerchantpay.gateway.genesisandroid.internal;

import com.emerchantpay.gateway.genesisandroid.api.internal.response.Response;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.models.Transaction;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseUnitTest {
    private Response response;
    private Transaction transaction;
    private GenesisError error;

    @Before
    public void setup() {
        response = mock(Response.class);
        transaction = mock(Transaction.class);
    }

    @Test
    public void testFailedResponse() {
        when(response.getTransaction()).thenReturn(transaction);
        when(response.getStatus()).thenReturn("error");

        // Init Genesis error
        error = new GenesisError(response.getStatus());

        when(response.isSuccess()).thenReturn(false);
        when(response.getError()).thenReturn(error);

        assertFalse(response.isSuccess());
        assertEquals(response.getError().getMessage(), "error");
    }

    @Test
    public void testSuccessResponse() {
        when(response.getTransaction()).thenReturn(transaction);
        when(response.getStatus()).thenReturn("approved");

        // Init Genesis error
        error = new GenesisError(response.getStatus());

        when(response.isSuccess()).thenReturn(true);
        when(response.getError()).thenReturn(error);

        assertTrue(response.isSuccess());
        assertEquals(response.getError().getMessage(), "approved");
    }
}
