package com.emerchantpay.gateway.genesisandroid.api.interfaces

import com.emerchantpay.gateway.genesisandroid.api.models.Country
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder



interface ShippingAddressAttributes {

    // Shipping PaymentAddress
    fun setShippingFirstname(firstname: String): ShippingAddressAttributes {
        requestBuilder.addElement("first_name", firstname)
        return this
    }

    fun setShippingLastname(lastname: String): ShippingAddressAttributes {
        requestBuilder.addElement("last_name", lastname)
        return this
    }

    fun setShippingPrimaryAddress(primaryAddress: String): ShippingAddressAttributes {
        requestBuilder.addElement("address1", primaryAddress)
        return this
    }

    fun setShippingSecondaryAddress(secondaryAddress: String): ShippingAddressAttributes {
        requestBuilder.addElement("address2", secondaryAddress)
        return this
    }

    fun setShippingCity(city: String): ShippingAddressAttributes {
        requestBuilder.addElement("city", city)
        return this
    }

    fun setShippingZipCode(zipCode: String): ShippingAddressAttributes {
        requestBuilder.addElement("zip_code", zipCode)
        return this
    }

    fun setShippingState(state: String): ShippingAddressAttributes {
        requestBuilder.addElement("state", state)
        return this
    }

    @Throws(IllegalAccessException::class)
    fun setShippingCountry(country: String): ShippingAddressAttributes {
        val c = Country()

        requestBuilder.addElement("country", c.getIsoCode(country)!!)

        return this
    }

    fun buildShippingAddress(): RequestBuilder {
        return requestBuilder
    }

    companion object {

        val requestBuilder = RequestBuilder("")
    }
}