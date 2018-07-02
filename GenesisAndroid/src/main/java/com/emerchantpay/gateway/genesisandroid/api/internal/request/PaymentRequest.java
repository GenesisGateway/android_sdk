package com.emerchantpay.gateway.genesisandroid.api.internal.request;

import android.content.Context;

import com.emerchantpay.gateway.genesisandroid.api.constants.SharedPrefConstants;
import com.emerchantpay.gateway.genesisandroid.api.constants.TransactionTypes;
import com.emerchantpay.gateway.genesisandroid.api.constants.URLConstants;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.BaseAttributes;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.RiskParamsAttributes;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.customerinfo.CustomerInfoAttributes;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.AsyncAttributes;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.DescriptorAttributes;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.PaymentAttributes;
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator;
import com.emerchantpay.gateway.genesisandroid.api.models.Currency;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;
import com.emerchantpay.gateway.genesisandroid.api.models.RiskParams;
import com.emerchantpay.gateway.genesisandroid.api.models.klarna.KlarnaItem;
import com.emerchantpay.gateway.genesisandroid.api.util.GenesisSharedPreferences;
import com.emerchantpay.gateway.genesisandroid.api.util.Request;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class PaymentRequest extends Request implements PaymentAttributes, CustomerInfoAttributes,
        DescriptorAttributes, AsyncAttributes, RiskParamsAttributes {

    // Request Builder
    private RequestBuilder requestBuilder;

    // Application context
    private Context context;

    private String transactionId;
    private String currency;
    private Integer exponent;
    private BigDecimal amount;
    private String description;
    private String notificationUrl;
    private String cancelUrl;
    private String usage;
    private String customerEmail;
    private String customerPhone;
    private Integer lifetime;
    private String customerGender;
    private BigDecimal orderTaxAmount;

    // Payment Addresses
    private PaymentAddress billingAddress;
    private PaymentAddress shippingAddress;

    // Transaction types
    private TransactionTypesRequest transactionTypes = new TransactionTypesRequest(this);

    // Risk params
    private RiskParams riskParams;

    // Klarna items
    private KlarnaItemsRequest klarnaItemsRequest;

    // Error handler
    private GenesisError error;

    // GenesisValidator
    private GenesisValidator validator = new GenesisValidator();

    // Shared preferences
    private GenesisSharedPreferences sharedPreferences = new GenesisSharedPreferences();

    public PaymentRequest(Context context, String transactionId, BigDecimal amount, Currency currency, String customerEmail,
                          String customerPhone, PaymentAddress billingAddress, String notificationUrl,
                          TransactionTypesRequest transactionTypes) throws IllegalAccessException {

        super();

        this.context = context;
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency.getCurrency();
        this.exponent = currency.getExponent();
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.billingAddress = billingAddress;
        this.transactionTypes = transactionTypes;

        if (notificationUrl != null && !notificationUrl.isEmpty()) {
            this.notificationUrl = notificationUrl;
        }

        // Init params
        loadParams();
        setBillingAddress(billingAddress);

        // Load shared preferences
        sharedPreferences.loadSharedPreferences(context, this);
    }

    public PaymentRequest() {
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

    public void loadParams() {
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

    public PaymentRequest setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
        return this;
    }

    public PaymentRequest setOrderTaxAmount(BigDecimal orderTaxAmount) {

        if (exponent > 0) {
            BigDecimal multiplyExp = new BigDecimal(Math.pow(10, exponent), MathContext.DECIMAL64);

            orderTaxAmount = amount.divide(multiplyExp);
        } else {
            orderTaxAmount = amount;
        }

        this.orderTaxAmount = orderTaxAmount;

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

    public KlarnaItemsRequest addKlarnaItem(KlarnaItem klarnaItem) {
        return klarnaItemsRequest = new KlarnaItemsRequest(klarnaItem);
    }

    public KlarnaItemsRequest addKlarnaItems(ArrayList<KlarnaItem> klarnaItems) {
        return klarnaItemsRequest = new KlarnaItemsRequest(klarnaItems);
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
            requestBuilder = new RequestBuilder(root)
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

            if (transactionTypes.getTransactionTypesList().contains(TransactionTypes.KLARNA_AUTHORIZE)) {
                requestBuilder.addElement("customer_gender", customerGender)
                        .addElement("order_tax_amount", orderTaxAmount);
            }

            if (klarnaItemsRequest != null) {
                requestBuilder.addElement(klarnaItemsRequest.toXML());
            }

            return requestBuilder;
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
        validator.isValidAddress(billingAddress);

        for (String transactionType : transactionTypes.getTransactionTypesList()) {
            switch (transactionType) {
                case "klarna_authorize":
                    if (validator.isValidKlarnaRequest(klarnaItemsRequest, amount, orderTaxAmount)
                            && validator.isValidRequest(this)) {
                        return true;
                    } else return false;
                default:
                    if (validator.isValidRequest(this)) {
                        return true;
                    } else {
                        return false;
                    }
            }
        }

        return null;
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

    public TransactionTypesRequest getTransactionTypes() {
        return transactionTypes;
    }

    public BigDecimal getOrderTaxAmount() {
        return orderTaxAmount;
    }

    public String getReturnSuccessUrl() {
        return URLConstants.SUCCESS_URL;
    }

    public String getReturnCancelUrl() {
        return URLConstants.CANCEL_URL;
    }

    public KlarnaItemsRequest getKlarnaItemsRequest() {
        return klarnaItemsRequest;
    }
}
