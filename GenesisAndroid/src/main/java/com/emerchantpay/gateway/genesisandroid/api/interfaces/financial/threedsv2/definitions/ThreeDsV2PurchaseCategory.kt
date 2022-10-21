package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions

enum class ThreeDsV2PurchaseCategory {
    GOODS,
    SERVICE,
    CHECK_ACCEPTANCE,
    ACCOUNT_FUNDING,
    QUASI_CASH,
    PREPAID_ACTIVATION,
    LOAN;

    override fun toString() = name.lowercase()
}