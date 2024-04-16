package com.emerchantpay.gateway.genesisandroid.api.constants

enum class Endpoints(val endpointName: String) {
    // Domain for E-ComProcessing's Genesis instance
    ECOMPROCESSING("e-comprocessing.net"),

    // Domain for Emerchantpay's Genesis instance
    EMERCHANTPAY("emerchantpay.net"),

    // Domains for Consumer API
    RETRIEVE_CONSUMER_EMERCHANTPAY("emerchantpay.net/v1/retrieve_consumer"),
    RETRIEVE_CONSUMER_ECOMPROCESSING("e-comprocessing.net/v1/retrieve_consumer");


    override fun toString(): String {
        return endpointName
    }
}