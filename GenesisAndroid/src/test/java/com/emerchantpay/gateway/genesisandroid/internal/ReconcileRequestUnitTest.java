package com.emerchantpay.gateway.genesisandroid.internal;

import android.content.Context;
import android.test.mock.MockContext;

import com.emerchantpay.gateway.genesisandroid.api.internal.request.ReconcileRequest;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ReconcileRequestUnitTest {

    private Context context;

    private ReconcileRequest request;
    private PaymentAddress address;

    private ArrayList<String> transactionTypes;

    @Before
    public void mockParams() throws IllegalAccessException, MalformedURLException {
        // Application context
        context = new MockContext().getApplicationContext();

        // Generate unique Id
        String uniqueId = UUID.randomUUID().toString();

        // Payment request
        request = new ReconcileRequest();
        request.setUniqueId(uniqueId);
    }

    @Test
    public void testReconcile() throws IllegalAccessException {
        assertEquals(request.getTransactionType(), "wpf_reconcile");
    }

    @Test
    public void testBuildRequest() {
        assertEquals(request.toXML(), request.getRequest().toXML());
    }
}
