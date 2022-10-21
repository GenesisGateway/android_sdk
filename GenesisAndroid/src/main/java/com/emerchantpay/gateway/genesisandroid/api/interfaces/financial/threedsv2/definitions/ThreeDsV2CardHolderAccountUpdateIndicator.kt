package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2CardHolderAccountUpdateIndicator(private val value: String) {
    UPDATE_CURRENT_TRANSACTION("current_transaction"),
    UPDATE_LESS_THAN_30DAYS("less_than_30days"),
    UPDATE_30_TO_60_DAYS("30_to_60_days"),
    UPDATE_MORE_THAN_60DAYS("more_than_60days");

    override fun toString(): String {
        return value
    }
}