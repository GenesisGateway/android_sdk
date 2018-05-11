package com.emerchantpay.gateway.genesisandroid.api.internal.request;

import android.content.Context;

import com.emerchantpay.gateway.genesisandroid.api.constants.SharedPrefConstants;
import com.emerchantpay.gateway.genesisandroid.api.constants.URLConstants;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.BaseAttributes;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.RiskParamsAttributes;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.customerinfo.CustomerInfoAttributes;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.AsyncAttributes;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.DescriptorAttributes;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.PaymentAttributes;
import com.emerchantpay.gateway.genesisandroid.api.internal.GenesisValidator;
import com.emerchantpay.gateway.genesisandroid.api.models.Currency;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;
import com.emerchantpay.gateway.genesisandroid.api.models.RiskParams;
import com.emerchantpay.gateway.genesisandroid.api.util.GenesisSharedPreferences;
import com.emerchantpay.gateway.genesisandroid.api.util.Request;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PaymentRequest extends Request implements PaymentAttributes, CustomerInfoAttributes,
        DescriptorAttributes, AsyncAttributes, RiskParamsAttributes {

    // Application context
    private Context context;

    private String transactionId;
    private String currency;
    private BigDecimal amount;
    private String description;
    private String notificationUrl;
    private String cancelUrl;
    private String usage;
    private String customerEmail;
    private String customerPhone;
    private Integer lifetime;

    // Payment Addresses
    private PaymentAddress billingAddress;
    private PaymentAddress shippingAddress;

    // Transaction types
    private TransactionTypesRequest transactionTypes = new TransactionTypesRequest(this);

    // Risk params
    private RiskParams riskParams;

    // Error handler
    private GenesisError error;

    // GenesisValidator
    private GenesisValidator validator = new GenesisValidator();

    // Shared preferences
    private GenesisSharedPreferences sharedPreferences = new GenesisSharedPreferences();

    public PaymentRequest(Context context, String transactionId, BigDecimal amount, Currency currency, String customerEmail,
                          String customerPhone, PaymentAddress billingAddress, String notificationUrl,
                          ArrayList<String> transactionTypesList) throws IllegalAccessException {
        super();

        this.context = context;
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency.getCurrency();
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.billingAddress = billingAddress;

        if (notificationUrl != null && !notificationUrl.isEmpty()) {
            this.notificationUrl = notificationUrl;
        } else {
            this.notificationUrl = null;
        }

        // Init params
        loadParams();
        setBillingAddress(billingAddress);
        loadTransactionTypes(transactionTypesList);

        // Load shared preferences
        sharedPreferences.loadSharedPreferences(context, this);
    }

    public void setBillingAddress(PaymentAddress address) throws IllegalAccessException {
        // Billing Payment Address
        setBillingFirstname(billingAddress.getFirstName());
        setBillingLastname(billingAddress.getLastname());
        setBillingPrimaryAddress(billingAddress.getAddress1());
        setBillingSecondaryAddress(billingAddress.getAddress2());
        setBillingZipCode(billingAddress.getZipCode());
        setBillingCity(billingAddress.getCity());
        setBillingState(billingAddress.getState());
        setBillingCountry(billingAddress.getCountryName());
    }


    public void setShippingAddress(PaymentAddress shippingAddress) throws IllegalAccessException {
        // Shipping Payment Address
        setShippingFirstname(shippingAddress.getFirstName());
        setShippingLastname(shippingAddress.getLastname());
        setShippingPrimaryAddress(shippingAddress.getAddress1());
        setShippingSecondaryAddress(shippingAddress.getAddress2());
        setShippingZipCode(shippingAddress.getZipCode());
        setShippingCity(shippingAddress.getCity());
        setShippingState(shippingAddress.getState());
        setShippingCountry(shippingAddress.getCountryName());
    }

    public void loadParams() throws IllegalAccessException {
        setTransactionId(transactionId);
        setAmount(amount);

        // Set currency
        if (currency != null) {
            setCurrency(currency);
        }

        // Customer info
        setCustomerEmail(customerEmail);
        setCustomerPhone(customerPhone);

        // Urls
        if (notificationUrl != null && !notificationUrl.isEmpty()) {
            setNotificationUrl(notificationUrl);
        }

        setReturnSuccessUrl(URLConstants.SUCCESS_URL);
        setReturnFailureUrl(URLConstants.FAILURE_URL);
        setReturnCancelUrl(URLConstants.CANCEL_URL);
    }

    public void loadTransactionTypes(ArrayList<String> transactionTypesList) throws IllegalAccessException {
        // Set validator
        transactionTypes.setValidator(validator);

        // Transaction Types
        for (String t : transactionTypesList) {
            addTransactionType(t);
        }
    }

    @Override
    public PaymentAttributes setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public PaymentAttributes setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public BaseAttributes setUsage(String usage) {
        this.usage = usage;
        sharedPreferences.putString(context, SharedPrefConstants.USAGE_KEY, usage);
        return this;
    }

    public PaymentRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public PaymentRequest setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
        return this;
    }

    public PaymentRequest setReturnCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
        return this;
    }

    public PaymentRequest setLifetime(Integer lifetime) {
        this.lifetime = lifetime;
        return this;
    }

    public PaymentRequest setRiskParams(RiskParams riskParams) {
        // Set params
        setRiskUserId(riskParams.getUserId());
        setRiskSessionId(riskParams.getSessionId());
        setRiskSSN(riskParams.getSsn());
        setRiskMacAddress(riskParams.getMacAddress());
        setRiskUserLevel(riskParams.getUserLevel());
        setRiskEmail(riskParams.getEmail());
        setRiskPhone(riskParams.getPhone());
        setRiskRemoteIp(riskParams.getRemoteIp());
        setRiskSerialNumber(riskParams.getSerialNumber());

        this.riskParams = riskParams;
        return this;
    }

    public TransactionTypesRequest addTransactionType(String transactionType) throws IllegalAccessException {

        transactionTypes.addTransaction(transactionType);
        return transactionTypes;
    }

    @Override
    public String getTransactionType() {
        return "wpf_payment";
    }

    @Override
    public String toXML() {
        return buildRequest("wpf_payment").toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {

        if (isValidData()) {
            return new RequestBuilder(root)
                    .addElement(buildBaseParams().toXML())
                    .addElement(buildPaymentParams().toXML())
                    .addElement(buildCustomerInfoParams().toXML())
                    .addElement("usage", usage)
                    .addElement("description", description)
                    .addElement("notification_url", notificationUrl)
                    .addElement(buildAsyncParams().toXML())
                    .addElement("return_cancel_url", cancelUrl)
                    .addElement("lifetime", lifetime)
                    .addElement("billing_address", buildBillingAddress().toXML())
                    .addElement("shipping_address", buildShippingAddress().toXML())
                    .addElement("transaction_types", transactionTypes)
                    .addElement("risk_params", buildRiskParams().toXML())
                    .addElement("dynamic_descriptor_params", buildDescriptorParams().toXML());
        } else {
            return new RequestBuilder(root);
        }
    }

    public GenesisError getError() {
        if (validator.getError() != null) {
            error = validator.getError();
        }

        return error;
    }

    public Boolean isValidData() {
        // Validate
        validator.isValidRequest(this);
        validator.isValidAddress(billingAddress);

        if (validator.isValidData()) {
            return true;
        } else {
            return false;
        }
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public String getUsage() {
        return usage;
    }

    public PaymentAddress getPaymentAddress() {
        return billingAddress;
    }

    public GenesisValidator getValidator() {
        return validator;
    }
}
