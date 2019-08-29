package com.emerchantpay.gateway.genesisandroid.api.constants

import java.io.Serializable

class Environments(val environmentName: String) : Serializable {

    override fun toString(): String {
        return environmentName
    }

    companion object {

        // Production Environments
        val PRODUCTION = Environments("gate")

        // Staging Environments
        val STAGING = Environments("staging.gate")
    }
}
