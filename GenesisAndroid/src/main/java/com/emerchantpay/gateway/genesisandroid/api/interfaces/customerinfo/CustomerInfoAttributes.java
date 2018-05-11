package com.emerchantpay.gateway.genesisandroid.api.interfaces.customerinfo;

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

public interface CustomerInfoAttributes {

    RequestBuilder requestBuilder = new RequestBuilder("");

    // Customer Info Params
    default CustomerInfoAttributes setCustomerEmail(String customerEmail) {
            requestBuilder.addElement("customer_email", customerEmail);
            return this;
    }

    default CustomerInfoAttributes setCustomerPhone(String customerPhone) {
        requestBuilder.addElement("customer_phone", customerPhone);
        return this;
    }

    default RequestBuilder buildCustomerInfoParams() {
        return requestBuilder;
    }
}