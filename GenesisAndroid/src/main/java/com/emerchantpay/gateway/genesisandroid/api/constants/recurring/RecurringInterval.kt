package com.emerchantpay.gateway.genesisandroid.api.constants

import java.util.*

enum class RecurringInterval(val value: String) {
    // Interval
    DAYS("days"),
    MONTHS("months");

    override fun toString(): String {
        return name.lowercase(Locale.ROOT)
    }
}