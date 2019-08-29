package com.emerchantpay.gateway.genesisandroid.api.constants

import java.io.Serializable

class Endpoints(val endpointName: String) : Serializable {

    override fun toString(): String {
        return endpointName
    }

    companion object {

        // Domain for E-ComProcessing's Genesis instance
        val ECOMPROCESSING = Endpoints("e-comprocessing.net")

        // Domain for Emerchantpay's Genesis instance
        val EMERCHANTPAY = Endpoints("emerchantpay.net")
    }
}
