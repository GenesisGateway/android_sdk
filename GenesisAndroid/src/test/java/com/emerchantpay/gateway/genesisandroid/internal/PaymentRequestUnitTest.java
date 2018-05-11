package com.emerchantpay.gateway.genesisandroid.internal;

import android.content.Context;
import android.test.mock.MockContext;

import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.models.Country;
import com.emerchantpay.gateway.genesisandroid.api.models.Currency;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;
import com.emerchantpay.gateway.genesisandroid.api.models.WPFTransactionTypes;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PaymentRequestUnitTest {

    private Context context;

    private PaymentRequest request;
    private PaymentAddress address;

    private ArrayList<String> transactionTypes;

    @Before
    public void mockParams() throws IllegalAccessException, MalformedURLException {
        // Application context
        context = new MockContext().getApplicationContext();

        // Generate unique Id
        String uniqueId = UUID.randomUUID().toString();

        // Address
        address = new PaymentAddress("John", "Doe", "Berlin street 1",
                "Berlin street 1", "10000", "Berlin",
                "Berlin state", Country.Germany);

        // Transaction types
        // Create Transaction types
        transactionTypes = new ArrayList<String>();
        transactionTypes.add(WPFTransactionTypes.sale);

        // Payment request
        request = new PaymentRequest(context, uniqueId,
                new BigDecimal("2.00"), Currency.USD,
                "john@example.com", "+55555555", address,
                "https://example.com", transactionTypes);

    }

    @Test
    public void testLoadParams() throws IllegalAccessException {
        request.loadParams();

        assertEquals(request.getCurrency(), "USD");
        assertEquals(request.getAmount().toString(), "2.00");
        assertEquals(request.getCustomerEmail(), "john@example.com");
        assertEquals(request.getCustomerPhone(), "+55555555");
        assertEquals(request.getNotificationUrl(), "https://example.com");
    }

    @Test
    public void testLoadAddress() throws IllegalAccessException {
        request.setBillingAddress(address);

        assertEquals(request.getPaymentAddress().getAddress1(), address.getAddress1());
        assertEquals(request.getPaymentAddress().getFirstName(), address.getFirstName());
        assertEquals(request.getPaymentAddress().getLastname(), address.getLastname());
        assertEquals(request.getPaymentAddress().getCountryCode(), address.getCountryCode());
        assertEquals(request.getPaymentAddress().getCountryName(), address.getCountryName());
    }

    @Test
    public void testLoadTransactionTypes() throws IllegalAccessException {
        request.loadTransactionTypes(transactionTypes);

        assertEquals(transactionTypes.get(0), "sale");
    }

    @Test
    public void testNotificationUrl() {
        assertEquals(request.getNotificationUrl(), "https://example.com");
    }

    @Test
    public void testEmptyNotificationUrl() throws MalformedURLException, IllegalAccessException {
        // Generate unique Id
        String uniqueId = UUID.randomUUID().toString();

        // Payment request
        request = new PaymentRequest(context, uniqueId,
                new BigDecimal("2.00"), Currency.USD,
                "john@example.com", "+55555555", address,
                "", transactionTypes);

        assertNull(request.getNotificationUrl());
    }

    @Test
    public void isValidRequestData() {
        assertTrue(request.isValidData());
    }

    @Test
    public void testGetTransactionType() {
        assertEquals(request.getTransactionType(), "wpf_payment");
    }

    @Test
    public void testBuildRequest() {
        assertEquals(request.toXML(), request.getRequest().toXML());
    }
}
