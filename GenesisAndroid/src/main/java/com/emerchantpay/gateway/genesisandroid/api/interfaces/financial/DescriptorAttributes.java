package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial;

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

public interface DescriptorAttributes {

    RequestBuilder requestBuilder = new RequestBuilder("");

    // Descriptor Params
    default DescriptorAttributes setDynamicMerchantName(String dynamicMerchantName) {
        requestBuilder.addElement("merchant_name", dynamicMerchantName);
        return this;
    }

    default DescriptorAttributes setDynamicMerchantCity(String dynamicMerchantCity) {
        requestBuilder.addElement("merchant_city", dynamicMerchantCity);
        return this;
    }

    default RequestBuilder buildDescriptorParams() {
        return requestBuilder;
    }
}