package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2MerchantRiskShippingIndicator {
    SAME_AS_BILLING,
    STORED_ADDRESS,
    VERIFIED_ADDRESS,
    PICK_UP,
    DIGITAL_GOODS,
    TRAVEL,
    EVENT_TICKETS,
    OTHER;

    override fun toString() = name.lowercase()
}