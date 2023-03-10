package com.emerchantpay.gateway.genesisandroid.api.constants

import java.util.*

enum class RecurringFrequency(val value: String) {
    // Frequency
    DAYLY("daily"),
    TWICE_WEEKLY("twice_weekly"),
    WEEKLY("weekly"),
    TEN_DAYS("ten_days"),
    FORTNIGHTLY("fortnightly"),
    MONTHLY("monthly"),
    EVERY_TWO_MONTHS("every_two_months"),
    TRIMESTER("trimester"),
    QUARTERLY("quarterly"),
    TWICE_YEARLY("twice_yearly"),
    ANUALLY("annually"),
    UNSCHEDULED("unscheduled");

    override fun toString(): String {
        return name.lowercase(Locale.ROOT)
    }
}