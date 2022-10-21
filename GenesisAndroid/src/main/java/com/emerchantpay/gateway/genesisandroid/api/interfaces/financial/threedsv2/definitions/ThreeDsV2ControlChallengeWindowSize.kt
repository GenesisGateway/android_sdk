package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2ControlChallengeWindowSize(private val value: String) {
    SIZE_250_400("250x400"),
    SIZE_390_400("390x400"),
    SIZE_500_600("500x600"),
    SIZE_600_400("600x400"),
    SIZE_FULL_SCREEN("full_screen");

    override fun toString(): String {
        return value
    }
}