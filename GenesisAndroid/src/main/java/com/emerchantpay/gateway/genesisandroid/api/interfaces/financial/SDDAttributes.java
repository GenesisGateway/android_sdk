package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial;

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

public interface SDDAttributes {

    RequestBuilder requestBuilder = new RequestBuilder("");

    // SDD Params
    default SDDAttributes setIban(String iban) {
        requestBuilder.addElement("iban", iban);
        return this;
    }

    default SDDAttributes setBic(String bic) {
        requestBuilder.addElement("bic", bic);
        return this;
    }

    default RequestBuilder buildSDDParams() {
        return requestBuilder;
    }
}
