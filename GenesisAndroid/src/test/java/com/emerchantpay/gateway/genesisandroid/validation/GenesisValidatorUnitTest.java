package com.emerchantpay.gateway.genesisandroid.validation;

import android.content.Context;
import android.test.mock.MockContext;

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.models.Country;
import com.emerchantpay.gateway.genesisandroid.api.models.Currency;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;
import com.emerchantpay.gateway.genesisandroid.api.models.WPFTransactionTypes;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GenesisValidatorUnitTest {

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
    private String testParam;

    @Before
    public void setup() throws IllegalAccessException {
        context = new MockContext().getApplicationContext();

        validator = new GenesisValidator();

        // Intitial params
        transactionId = UUID.randomUUID().toString();
        amount = new BigDecimal("2.00");
        customerEmail = "johndoe@example.com";
        customerPhone = "+555555555";
        notificationUrl = "http://google.com";

        // Test param
        testParam = "test_param";

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


    // Transaction Id
    @Test
    public void testTransactionId() {
        assertTrue(validator.validateString("transaction_id", transactionId));

        // If Transaction Id is empty
        transactionId = "";
        assertFalse(validator.validateString("transaction_id", transactionId));
    }

    // Amount
    @Test
    public void testAmountValidationSuccess() {
        assertTrue(validator.validateAmount(amount));

        amount = new BigDecimal("0.99");
        assertTrue(validator.validateAmount(amount));
    }

    @Test
    public void testAmountValidationFailed() {
        amount = new BigDecimal("0.00");
        assertFalse(validator.validateAmount(amount));

        error = new GenesisError(ErrorMessages.INVALID_AMOUNT);
        assertEquals(error.getMessage(), ErrorMessages.INVALID_AMOUNT);
    }

    // Empty params
    @Test
    public void testStringValidationSuccess() {
        assertTrue(validator.validateString("test_param", testParam));
    }

    @Test
    public void testStringValidationFailed() {
        testParam = "";
        assertFalse(validator.validateString("test_param", testParam));

        error = new GenesisError(ErrorMessages.EMPTY_PARAM, testParam);
        assertEquals(error.getMessage(), ErrorMessages.EMPTY_PARAM);
        assertEquals(error.getTechnicalMessage(), testParam);
    }

    // Email
    @Test
    public void testEmailValidationSuccess() {
        assertTrue(validator.validateEmail(customerEmail));
    }

    @Test
    public void testEmailValidationFailed() {
        customerEmail = "johndoe.example.com";
        assertFalse(validator.validateEmail(customerEmail));

        error = new GenesisError(ErrorMessages.INVALID_EMAIL, customerEmail);
        assertEquals(error.getMessage(), ErrorMessages.INVALID_EMAIL);
        assertEquals(error.getTechnicalMessage(), customerEmail);
    }

    // Phone
    @Test
    public void testPhoneValidationSuccess() {
        assertTrue(validator.validatePhone(customerPhone));
    }

    @Test
    public void testPhoneValidationFailed() {
        customerPhone = "test555555555";
        assertFalse(validator.validatePhone(customerPhone));

        error = new GenesisError(ErrorMessages.INVALID_PHONE, customerPhone);
        assertEquals(error.getMessage(), ErrorMessages.INVALID_PHONE);
        assertEquals(error.getTechnicalMessage(), customerPhone);
    }

    // URLs
    @Test
    public void testUrlValidationSuccess() {
        assertTrue(validator.validateNotificationUrl(notificationUrl));
    }

    @Test
    public void testUrlValidationFailed() {
        notificationUrl = ":// should fail";
        assertFalse(validator.validateNotificationUrl(notificationUrl));

        notificationUrl = ".";
        assertFalse(validator.validateNotificationUrl(notificationUrl));

        notificationUrl = "google.com";
        assertFalse(validator.validateNotificationUrl(notificationUrl));

        error = new GenesisError(ErrorMessages.INVALID_NOTIFICATION_URL, notificationUrl);
        assertEquals(error.getMessage(), ErrorMessages.INVALID_NOTIFICATION_URL);
        assertEquals(error.getTechnicalMessage(), notificationUrl.toString());
    }

    @Test
    public void testIsRequestValid() {
        assertTrue(validator.isValidRequest(request));
    }

    @Test
    public void testIsAddressValid() {
        assertTrue(validator.isValidAddress(billingAddress));
    }

    @Test
    public void testTransactionTypeValidionSuccess() throws IllegalAccessException {
        assertTrue(validator.validateTransactionType(WPFTransactionTypes.authorize));
    }

    @Test
    public void testTransactionTypeValidionFailed() throws IllegalAccessException {
        assertFalse(validator.validateTransactionType("test_transaction_type"));
    }

    @Test
    public void testIsValidData() {
        assertTrue(validator.isValidRequest(request));
    }
}
