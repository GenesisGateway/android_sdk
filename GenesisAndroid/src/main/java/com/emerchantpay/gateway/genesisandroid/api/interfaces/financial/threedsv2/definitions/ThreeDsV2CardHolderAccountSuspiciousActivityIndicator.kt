package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2CardHolderAccountSuspiciousActivityIndicator {
    NO_SUSPICIOUS_OBSERVED,
    SUSPICIOUS_OBSERVED;

    override fun toString() = name.lowercase()
}