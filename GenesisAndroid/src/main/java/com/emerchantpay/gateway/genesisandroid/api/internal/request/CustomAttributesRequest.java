package com.emerchantpay.gateway.genesisandroid.api.internal.request;

import com.emerchantpay.gateway.genesisandroid.api.util.Request;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilderWithAttribute;

import java.util.HashMap;

public class CustomAttributesRequest extends Request {

    private TransactionTypesRequest parent;

    private String transactionType;

    private HashMap<String, String> paramsMap = new HashMap<String, String>();

    public CustomAttributesRequest() {
        super();
    }

    public CustomAttributesRequest(TransactionTypesRequest parent, String transactionType) {
        this.parent = parent;
        this.transactionType = transactionType;
    }

    public CustomAttributesRequest addAttribute(String key, String value) {
        paramsMap.put(key, value);
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("transaction_type").toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilderWithAttribute buildRequest(String root) {

        RequestBuilderWithAttribute builder;

        builder = new RequestBuilderWithAttribute(root, transactionType);

        for (String key : paramsMap.keySet()) {
            builder.addElement(key, paramsMap.get(key));
        }

        return builder;
    }

    public HashMap<String, String> getParamsMap() {
        return paramsMap;
    }

    @Override
    public String getTransactionType() {
        return transactionType;
    }

    public TransactionTypesRequest done() {
        return parent;
    }
}
