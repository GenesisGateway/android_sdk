package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2ControlDeviceType {
    BROWSER,
    APPLICATION;

    override fun toString() = name.lowercase()
}