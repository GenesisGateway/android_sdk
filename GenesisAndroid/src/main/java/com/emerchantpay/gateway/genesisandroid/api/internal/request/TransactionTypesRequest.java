package com.emerchantpay.gateway.genesisandroid.api.internal.request;


import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator;
import com.emerchantpay.gateway.genesisandroid.api.util.Request;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionTypesRequest extends Request {

    private PaymentRequest parent;
    private ArrayList<String> transactionTypesList = new ArrayList<String>();
    private CustomAttributesRequest customAttributes;
    private ArrayList<CustomAttributesRequest> customAttributesList = new ArrayList<CustomAttributesRequest>();

    // Genesis Validator
    private GenesisValidator validator = new GenesisValidator();

    public TransactionTypesRequest() {
        super();
    }

    public TransactionTypesRequest(PaymentRequest parent) {
        this.parent = parent;
    }

    public TransactionTypesRequest addTransaction(String transactionType) throws IllegalAccessException {
        // validate transaction type
        validator.validateTransactionType(transactionType);

        transactionTypesList.add(transactionType);

        customAttributes = new CustomAttributesRequest(this, transactionType);
        customAttributesList.add(customAttributes);

        return this;
    }

    public TransactionTypesRequest addTransaction(String transactionType, HashMap<String, String> additionalParams) throws IllegalAccessException {
        // validate transaction type
        validator.validateTransactionType(transactionType);

        transactionTypesList.add(transactionType);

        customAttributes = new CustomAttributesRequest(this, transactionType);

        for (String key : additionalParams.keySet()) {
            customAttributes.addAttribute(key, additionalParams.get(key));
        }

        customAttributesList.add(customAttributes);

        return this;
    }

    public TransactionTypesRequest addParam(String key, String value) {
        this.customAttributes.addAttribute(key, value);
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("transaction_types").toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {

        RequestBuilder builder = new RequestBuilder(root);

        for (CustomAttributesRequest attribute : customAttributesList) {
            builder.addElement(null, attribute);
        }

        return builder;
    }

    public CustomAttributesRequest getCustomAttributes() {
        return customAttributes;
    }

    public ArrayList<CustomAttributesRequest> getCustomAttributesList() {
        return customAttributesList;
    }

    public ArrayList<String> getTransactionTypesList() {
        return transactionTypesList;
    }

    public PaymentRequest done() {
        return parent;
    }
}
