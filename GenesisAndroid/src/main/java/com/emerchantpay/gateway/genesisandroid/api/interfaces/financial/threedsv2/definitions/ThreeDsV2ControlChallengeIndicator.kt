package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2ControlChallengeIndicator {
    NO_PREFERENCE,
    NO_CHALLENGE_REQUESTED,
    PREFERENCE,
    MANDATE;

    override fun toString() = name.lowercase()
}