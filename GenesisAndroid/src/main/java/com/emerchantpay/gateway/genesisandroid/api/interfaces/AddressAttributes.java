package com.emerchantpay.gateway.genesisandroid.api.interfaces;

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

public interface AddressAttributes extends BillingAddressAttributes, ShippingAddressAttributes {
    default RequestBuilder getBillingAddress() {
        return buildBillingAddress();
    }

    default RequestBuilder getShippingAddress() {
        return buildShippingAddress();
    }
}