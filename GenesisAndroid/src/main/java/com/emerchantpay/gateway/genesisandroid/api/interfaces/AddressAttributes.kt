package com.emerchantpay.gateway.genesisandroid.api.interfaces

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface AddressAttributes : BillingAddressAttributes, ShippingAddressAttributes {

    fun getBillingAddress(): RequestBuilder {
        return buildBillingAddress()
    }

    fun getShippingAddress(): RequestBuilder {
        return buildShippingAddress()
    }
}