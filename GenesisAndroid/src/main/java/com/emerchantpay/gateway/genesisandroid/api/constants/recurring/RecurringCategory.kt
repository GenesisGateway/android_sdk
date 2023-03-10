package com.emerchantpay.gateway.genesisandroid.api.constants.recurring

import java.util.*

enum class RecurringCategory(val value: String) {
    SUBSCRIPTION("subscription"),
    STANDING_ORDER("stanging_order");

    override fun toString(): String {
        return name.lowercase(Locale.ROOT)
    }
}