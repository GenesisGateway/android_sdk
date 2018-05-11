package com.emerchantpay.gateway.genesisandroid;

import com.emerchantpay.gateway.genesisandroid.api.models.Transaction;
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionUnitTest {

    private NodeWrapper wrapper;
    private Transaction transaction;

    @Before
    public void setup() {
        wrapper = mock(NodeWrapper.class);
    }

    @Test
    public void testTransaction() {
        when(wrapper.findString("status")).thenReturn("approved");
        when(wrapper.findBigDecimal("amount")).thenReturn(new BigDecimal("2.00"));

        transaction = new Transaction(wrapper);

        assertEquals(transaction.getStatus(), wrapper.findString("status"));
        assertEquals(transaction.getAmount(), wrapper.findBigDecimal("amount"));
    }
}
