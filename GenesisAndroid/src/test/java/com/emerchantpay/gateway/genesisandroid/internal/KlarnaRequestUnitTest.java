package com.emerchantpay.gateway.genesisandroid.internal;

import android.content.Context;
import android.test.mock.MockContext;

import com.emerchantpay.gateway.genesisandroid.api.constants.KlarnaItemTypes;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.models.Country;
import com.emerchantpay.gateway.genesisandroid.api.models.Currency;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;
import com.emerchantpay.gateway.genesisandroid.api.models.WPFTransactionTypes;
import com.emerchantpay.gateway.genesisandroid.api.models.klarna.KlarnaItem;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class KlarnaRequestUnitTest {

    // Application context
    private Context context;

    // Genesis Validator
    private GenesisValidator validator;

    // Genesis Error handler
    private GenesisError error;

    // Payment request
    private PaymentRequest request;

    // Payment address
    private PaymentAddress billingAddress;

    // Transaction types
    private TransactionTypesRequest transactionTypes;

    // Parameters
    private String transactionId;
    private BigDecimal amount;
    private String customerEmail;
    private String customerPhone;
    private String notificationUrl;

    @Before
    public void setupOneItem() throws IllegalAccessException {
        context = new MockContext().getApplicationContext();

        validator = new GenesisValidator();

        // Intitial params
        transactionId = UUID.randomUUID().toString();
        amount = new BigDecimal("4.00");
        customerEmail = "johndoe@example.com";
        customerPhone = "+555555555";
        notificationUrl = "http://google.com";

        // Address
        billingAddress = new PaymentAddress("John", "Doe",
                "address1", "","10000", "New York",
                "state", new Country().getCountry("United States"));

        // Transaction types list
        transactionTypes = new TransactionTypesRequest();
        transactionTypes.addTransaction(WPFTransactionTypes.klarnaAuthorize);

        // Payment request
        request = new PaymentRequest(context, transactionId, amount, Currency.USD,
                customerEmail, customerPhone, billingAddress, notificationUrl, transactionTypes);
    }

    @Test
    public void testWithOneItem() {
        KlarnaItem klarnaItem = new KlarnaItem("TICKETS", KlarnaItemTypes.PHYSICAL, 2,
                new BigDecimal("2.00"), new BigDecimal("4.00"));

        request.addKlarnaItem(klarnaItem);

        assertTrue(validator.isValidKlarnaRequest(request.getKlarnaItemsRequest(), amount, request.getOrderTaxAmount()));
    }

    @Test
    public void testWithMoreThanOneItem() throws IllegalAccessException {
        amount = new BigDecimal("6.00");

        // Payment request
        request = new PaymentRequest(context, transactionId, amount, Currency.USD,
                customerEmail, customerPhone, billingAddress, notificationUrl, transactionTypes);

        // Klarna Item 1
        KlarnaItem item1 = new KlarnaItem("TICKETS", KlarnaItemTypes.PHYSICAL, 2,
                new BigDecimal("2.00"), new BigDecimal("4.00"));

        // Klarna Item 2
        KlarnaItem item2 = new KlarnaItem("TICKETS", KlarnaItemTypes.DISCOUNT, 2,
                new BigDecimal("2.00"), new BigDecimal("2.00"));
        item2.setTotalDiscountAmount(new BigDecimal("2.00"));

        ArrayList<KlarnaItem> klarnaItemsList = new ArrayList<KlarnaItem>();
        klarnaItemsList.add(item1);
        klarnaItemsList.add(item2);

        request.addKlarnaItems(klarnaItemsList);

        assertTrue(validator.isValidKlarnaRequest(request.getKlarnaItemsRequest(), amount, request.getOrderTaxAmount()));
    }

    @Test
    public void testWithoutItems() {
        assertFalse(validator.isValidKlarnaRequest(request.getKlarnaItemsRequest(), amount, request.getOrderTaxAmount()));
    }
}
