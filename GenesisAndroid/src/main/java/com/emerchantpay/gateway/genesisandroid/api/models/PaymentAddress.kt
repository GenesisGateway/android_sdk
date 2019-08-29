package com.emerchantpay.gateway.genesisandroid.api.models

class PaymentAddress(val firstName: String, val lastname: String, val address1: String, val address2: String,
                     val zipCode: String, val city: String, val state: String, private val country: Country) {

    val countryName: String
        get() = country.countryName

    val countryCode: String
        get() = country.code
}
