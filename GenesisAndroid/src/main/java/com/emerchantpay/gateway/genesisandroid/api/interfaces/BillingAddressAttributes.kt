package com.emerchantpay.gateway.genesisandroid.api.interfaces

import com.emerchantpay.gateway.genesisandroid.api.models.Country
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder


interface BillingAddressAttributes {

    // Billing PaymentAddress
    fun setBillingFirstname(firstname: String): BillingAddressAttributes {
        requestBuilder.addElement("first_name", firstname)
        return this
    }

    fun setBillingLastname(lastname: String): BillingAddressAttributes {
        requestBuilder.addElement("last_name", lastname)
        return this
    }

    fun setBillingPrimaryAddress(primaryAddress: String): BillingAddressAttributes {
        requestBuilder.addElement("address1", primaryAddress)
        return this
    }

    fun setBillingSecondaryAddress(secondaryAddress: String): BillingAddressAttributes {
        requestBuilder.addElement("address2", secondaryAddress)
        return this
    }

    fun setBillingCity(city: String): BillingAddressAttributes {
        requestBuilder.addElement("city", city)
        return this
    }

    fun setBillingZipCode(zipCode: String): BillingAddressAttributes {
        requestBuilder.addElement("zip_code", zipCode)
        return this
    }

    fun setBillingState(state: String): BillingAddressAttributes {
        requestBuilder.addElement("state", state)
        return this
    }

    @Throws(IllegalAccessException::class)
    fun setBillingCountry(country: String): BillingAddressAttributes {
        val c = Country()

        requestBuilder.addElement("country", c.getIsoCode(country)!!)

        return this
    }

    fun buildBillingAddress(): RequestBuilder {
        return requestBuilder
    }

    companion object {
        val requestBuilder = RequestBuilder("")
    }
}