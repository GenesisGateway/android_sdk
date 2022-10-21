package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2MerchantRiskDeliveryTimeframe {
    ELECTRONIC,
    SAME_DAY,
    OVER_NIGHT,
    ANOTHER_DAY;

    override fun toString() = name.lowercase()
}