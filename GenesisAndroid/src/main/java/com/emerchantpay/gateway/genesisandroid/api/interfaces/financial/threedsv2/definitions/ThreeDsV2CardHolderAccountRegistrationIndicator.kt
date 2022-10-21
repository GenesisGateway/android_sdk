package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2CardHolderAccountRegistrationIndicator(private val value: String) {
    REGISTRATION_GUEST_CHECKOUT("guest_checkout"),
    REGISTRATION_CURRENT_TRANSACTION("current_transaction"),
    REGISTRATION_LESS_THAN_30DAYS("less_than_30days"),
    REGISTRATION_30_TO_60_DAYS("30_to_60_days"),
    REGISTRATION_MORE_THAN_60DAYS("more_than_60days");

    override fun toString(): String {
        return value
    }
}