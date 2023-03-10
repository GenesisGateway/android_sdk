package com.emerchantpay.gateway.genesisandroid.api.constants

import java.util.*

enum class RecurringPaymentType(val value: String) {
    // Payment types
    INITIAL("initial"),
    SUBSEQUENT("subsequent"),
    MODIFICATION("modification"),
    CANCELLATION("cancellation");

    override fun toString(): String {
        return name.lowercase(Locale.ROOT)
    }
}