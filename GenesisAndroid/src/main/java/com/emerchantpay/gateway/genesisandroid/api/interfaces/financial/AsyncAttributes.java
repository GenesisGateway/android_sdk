package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial;

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

public interface AsyncAttributes {

    RequestBuilder requestBuilder = new RequestBuilder("");

    // Async Params
    default AsyncAttributes setReturnSuccessUrl(String successUrl) {
        requestBuilder.addElement("return_success_url", successUrl);
        return this;
    }

    default AsyncAttributes setReturnFailureUrl(String failureUrl) {
        requestBuilder.addElement("return_failure_url", failureUrl);
        return this;
    }

    default RequestBuilder buildAsyncParams() {
        return requestBuilder;
    }
}
