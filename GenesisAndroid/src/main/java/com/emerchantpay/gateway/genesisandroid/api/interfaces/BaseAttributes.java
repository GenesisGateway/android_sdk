package com.emerchantpay.gateway.genesisandroid.api.interfaces;

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

public interface BaseAttributes {

    RequestBuilder requestBuilder = new RequestBuilder("");

    // Base Params
    default BaseAttributes setTransactionId(String transactionId) {
        requestBuilder.addElement("transaction_id", transactionId);
        return this;
    }

    default BaseAttributes setUsage(String usage) {
        requestBuilder.addElement("usage", usage);
        return this;
    }

    default BaseAttributes setRemoteIp(String remoteIP) {
        requestBuilder.addElement("remote_ip", remoteIP);
        return this;
    }

    default RequestBuilder buildBaseParams() {
        return requestBuilder;
    }
}