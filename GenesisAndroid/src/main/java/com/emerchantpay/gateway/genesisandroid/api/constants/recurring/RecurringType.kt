package com.emerchantpay.gateway.genesisandroid.api.constants.recurring

import java.util.*

enum class RecurringType(val value: String) {
    INITIAL("initial"),
    MANAGED("managed");

    override fun toString(): String {
        return name.lowercase(Locale.ROOT)
    }
}