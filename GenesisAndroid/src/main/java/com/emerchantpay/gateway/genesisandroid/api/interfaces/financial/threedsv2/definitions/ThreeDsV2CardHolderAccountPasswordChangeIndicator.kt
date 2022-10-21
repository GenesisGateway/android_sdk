package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2CardHolderAccountPasswordChangeIndicator(private val value: String) {
    PASSWORD_CHANGE_NO_CHANGE("no_change"),
    PASSWORD_CHANGE_DURING_TRANSACTION("during_transaction"),
    PASSWORD_CHANGE_LESS_THAN_30DAYS("less_than_30days"),
    PASSWORD_CHANGE_30_TO_60_DAYS("30_to_60_days"),
    PASSWORD_CHANGE_MORE_THAN_60DAYS("more_than_60days");

    override fun toString(): String {
        return value
    }
}