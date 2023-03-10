package com.emerchantpay.gateway.genesisandroid.api.constants

import java.util.*

enum class RecurringAmountType(val value: String) {
    // Amount types
    FIXED("fixed"),
    MAX("max");

    override fun toString(): String {
        return name.lowercase(Locale.ROOT)
    }
}