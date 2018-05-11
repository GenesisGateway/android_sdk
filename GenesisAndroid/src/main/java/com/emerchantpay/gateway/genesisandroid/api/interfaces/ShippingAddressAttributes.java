package com.emerchantpay.gateway.genesisandroid.api.interfaces;

import com.emerchantpay.gateway.genesisandroid.api.models.Country;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

public interface ShippingAddressAttributes {

    RequestBuilder requestBuilder = new RequestBuilder("");

    // Shipping PaymentAddress
    default ShippingAddressAttributes setShippingFirstname(String firstname) {
        requestBuilder.addElement("first_name", firstname);
        return this;
    }

    default ShippingAddressAttributes setShippingLastname(String lastname) {
        requestBuilder.addElement("last_name", lastname);
        return this;
    }

    default ShippingAddressAttributes setShippingPrimaryAddress(String primaryAddress) {
        requestBuilder.addElement("address1", primaryAddress);
        return this;
    }

    default ShippingAddressAttributes setShippingSecondaryAddress(String secondaryAddress) {
        requestBuilder.addElement("address2", secondaryAddress);
        return this;
    }

    default ShippingAddressAttributes setShippingCity(String city) {
        requestBuilder.addElement("city", city);
        return this;
    }

    default ShippingAddressAttributes setShippingZipCode(String zipCode) {
        requestBuilder.addElement("zip_code", zipCode);
        return this;
    }

    default ShippingAddressAttributes setShippingState(String state) {
        requestBuilder.addElement("state", state);
        return this;
    }

    default ShippingAddressAttributes setShippingCountry(String country) throws IllegalAccessException {
        Country c = new Country();

        requestBuilder.addElement("country", c.getIsoCode(country));

        return this;
    }

    default RequestBuilder buildShippingAddress() {
        return requestBuilder;
    }
}