package com.emerchantpay.gateway.genesisandroid.validation;

import android.content.Context;
import android.test.mock.MockContext;

import com.emerchantpay.gateway.genesisandroid.api.constants.KlarnaItemTypes;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.RequiredParameters;
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.RequiredParametersValidator;
import com.emerchantpay.gateway.genesisandroid.api.models.Country;
import com.emerchantpay.gateway.genesisandroid.api.models.Currency;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;
import com.emerchantpay.gateway.genesisandroid.api.models.WPFTransactionTypes;
import com.emerchantpay.gateway.genesisandroid.api.models.klarna.KlarnaItem;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RequiredParametersValidatorUnitTest {

    // Application context
    private Context context;

    // Genesis Validator
    private RequiredParameters requiredParameters = new RequiredParameters();
    private RequiredParametersValidator validator;

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

    private HashMap<String, String> requiredParamsMap;

    @Before
    public void setupValidData() throws IllegalAccessException {
        context = new MockContext().getApplicationContext();

        // Intitial params
        transactionId = UUID.randomUUID().toString();
        amount = new BigDecimal("2.00");
        customerEmail = "johndoe@example.com";
        customerPhone = "+555555555";
        notificationUrl = "http://google.com";

        // Address
        billingAddress = new PaymentAddress("John", "Doe",
                "address1", "", "10000", "New York",
                "state", new Country().getCountry("United States"));

        // Transaction types list
        transactionTypes = new TransactionTypesRequest();
        transactionTypes.addTransaction(WPFTransactionTypes.authorize);
        transactionTypes.addTransaction(WPFTransactionTypes.ezeewallet);

        // Payment request
        request = new PaymentRequest(context, transactionId, amount, Currency.USD,
                customerEmail, customerPhone, billingAddress, notificationUrl, transactionTypes);
    }


    // Request params
    @Test
    public void testRequestParams() {
        HashMap<String, String> map = requiredParameters.getRequiredParametersForRequest(request);

        validator = new RequiredParametersValidator(map);
        assertTrue(validator.isValidRequiredParams());
    }

    @Test
    public void testWithMissingRequestParams() throws IllegalAccessException {
        // Intitial params
        transactionId = UUID.randomUUID().toString();
        amount = new BigDecimal("2.00");
        customerEmail = null;
        customerPhone = "+555555555";
        notificationUrl = "http://google.com";

        // Payment request
        request = new PaymentRequest(context, transactionId, amount, Currency.USD,
                customerEmail, customerPhone, billingAddress, notificationUrl, transactionTypes);

        requiredParamsMap = requiredParameters.getRequiredParametersForRequest(request);

        validator = new RequiredParametersValidator(requiredParamsMap);
        assertFalse(validator.isValidRequiredParams());
    }

    // Billing address params
    @Test
    public void testAddressParams() {
        HashMap<String, String> map = requiredParameters.getRequiredParametersForAddress(billingAddress);

        validator = new RequiredParametersValidator(map);
        assertTrue(validator.isValidRequiredParams());
    }

    @Test
    public void testWithMissingAddressParams() throws IllegalAccessException {
        // Address
        billingAddress = new PaymentAddress("John", "Doe",
                null, "", "10000", "New York",
                "state", new Country().getCountry("United States"));

        requiredParamsMap = requiredParameters.getRequiredParametersForAddress(billingAddress);

        validator = new RequiredParametersValidator(requiredParamsMap);
        assertFalse(validator.isValidRequiredParams());
    }

    // Transaction types params
    @Test
    public void testWithTransactionTypesParams() throws IllegalAccessException {
        // Transaction types list
        transactionTypes = new TransactionTypesRequest();
        transactionTypes.addTransaction(WPFTransactionTypes.ppro);
        transactionTypes.addParam("product_name", "TICKETS");

        // Payment request
        request = new PaymentRequest(context, transactionId, amount, Currency.USD,
                customerEmail, customerPhone, billingAddress, notificationUrl, transactionTypes);

        for (String transactionType : transactionTypes.getTransactionTypesList()) {
            HashMap<String, String> map = requiredParameters.getRequiredParametersForTransactionType(request, transactionType);

            validator = new RequiredParametersValidator(map);

            assertTrue(validator.isValidRequiredParams());
        }
    }

    @Test
    public void testWithMissingTransactionTypesParams() throws IllegalAccessException {
        // Transaction types list
        transactionTypes = new TransactionTypesRequest();
        transactionTypes.addTransaction(WPFTransactionTypes.ppro);

        // Payment request
        request = new PaymentRequest(context, transactionId, amount, Currency.USD,
                customerEmail, customerPhone, billingAddress, notificationUrl, transactionTypes);

        HashMap<String, String> map;

        for (String transactionType : transactionTypes.getTransactionTypesList()) {
            map = requiredParameters.getRequiredParametersForTransactionType(request, transactionType);

            validator = new RequiredParametersValidator(map);

            assertFalse(validator.isValidRequiredParams());
        }
    }

    // Klarna params
    @Test
    public void testWithKlarnaItems() {
        KlarnaItem item = new KlarnaItem("TICKET", KlarnaItemTypes.DISCOUNT, 10,
                new BigDecimal(10.00), new BigDecimal(2.00));

        request.addKlarnaItem(item);

        requiredParamsMap = requiredParameters.getRequiredParametersForKlarnaItem(request.getKlarnaItemsRequest());

        validator = new RequiredParametersValidator(requiredParamsMap);
        assertTrue(validator.isValidRequiredParams());
    }

    @Test
    public void testWithMissingKlarnaItems() {
        KlarnaItem item = new KlarnaItem(null, KlarnaItemTypes.DISCOUNT, 10,
                new BigDecimal(10.00), new BigDecimal(2.00));

        request.addKlarnaItem(item);

        HashMap<String, String> map = requiredParameters.getRequiredParametersForKlarnaItem(request.getKlarnaItemsRequest());

        validator = new RequiredParametersValidator(map);
        assertFalse(validator.isValidRequiredParams());
    }
}