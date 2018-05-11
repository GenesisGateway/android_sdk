package com.emerchantpay.gateway.genesisandroid.api.models;

public class DynamicDescriptorParams {

    private String merchantName;
    private String merchantCity;

    public DynamicDescriptorParams(String merchantName, String merchantCity) {
        super();

        this.merchantName = merchantName;
        this.merchantCity = merchantCity;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantCity() {
        return merchantCity;
    }
}