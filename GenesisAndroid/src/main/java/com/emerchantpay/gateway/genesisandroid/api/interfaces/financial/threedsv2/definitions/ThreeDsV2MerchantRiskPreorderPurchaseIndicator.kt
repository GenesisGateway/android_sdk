package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2MerchantRiskPreorderPurchaseIndicator {
    MERCHANDISE_AVAILABLE,
    FUTURE_AVAILABILITY;

    override fun toString() = name.lowercase()
}