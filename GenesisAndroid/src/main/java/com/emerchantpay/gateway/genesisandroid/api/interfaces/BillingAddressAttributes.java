package com.emerchantpay.gateway.genesisandroid.api.interfaces;

import com.emerchantpay.gateway.genesisandroid.api.models.Country;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

public interface BillingAddressAttributes {

    RequestBuilder requestBuilder = new RequestBuilder("");

    // Billing PaymentAddress
    default BillingAddressAttributes setBillingFirstname(String firstname) {
        requestBuilder.addElement("first_name", firstname);
        return this;
    }

    default BillingAddressAttributes setBillingLastname(String lastname) {
        requestBuilder.addElement("last_name", lastname);
        return this;
    }

    default BillingAddressAttributes setBillingPrimaryAddress(String primaryAddress) {
        requestBuilder.addElement("address1", primaryAddress);
        return this;
    }

    default BillingAddressAttributes setBillingSecondaryAddress(String secondaryAddress) {
        requestBuilder.addElement("address2", secondaryAddress);
        return this;
    }

    default BillingAddressAttributes setBillingCity(String city) {
        requestBuilder.addElement("city", city);
        return this;
    }

    default BillingAddressAttributes setBillingZipCode(String zipCode) {
        requestBuilder.addElement("zip_code", zipCode);
        return this;
    }

    default BillingAddressAttributes setBillingState(String state) {
        requestBuilder.addElement("state", state);
        return this;
    }

    default BillingAddressAttributes setBillingCountry(String country) throws IllegalAccessException {
        Country c = new Country();

        requestBuilder.addElement("country", c.getIsoCode(country));

        return this;
    }

    default RequestBuilder buildBillingAddress() {
        return requestBuilder;
    }
}