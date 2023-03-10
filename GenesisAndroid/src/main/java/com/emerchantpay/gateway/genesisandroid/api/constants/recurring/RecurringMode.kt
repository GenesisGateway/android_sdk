package com.emerchantpay.gateway.genesisandroid.api.constants

import java.util.*

enum class RecurringMode(val value: String) {
    // Mode
    AUTOMATIC("automatic"),
    MANUAL("manual");

    override fun toString(): String {
        return name.lowercase(Locale.ROOT)
    }
}