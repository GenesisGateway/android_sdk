package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2MerchantRiskReorderItemsIndicator {
    FIRST_TIME,
    REORDERED;

    override fun toString() = name.lowercase()
}