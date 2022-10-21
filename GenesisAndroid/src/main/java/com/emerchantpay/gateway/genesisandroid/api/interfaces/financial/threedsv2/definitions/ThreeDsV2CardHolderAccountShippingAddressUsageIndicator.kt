package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2CardHolderAccountShippingAddressUsageIndicator(private val value: String) {
    ADDRESS_USAGE_CURRENT_TRANSACTION("current_transaction"),
    ADDRESS_USAGE_LESS_THAN_30DAYS("less_than_30days"),
    ADDRESS_USAGE_30_TO_60_DAYS("30_to_60_days"),
    ADDRESS_USAGE_MORE_THAN_60DAYS("more_than_60days");

    override fun toString(): String {
        return value
    }
}