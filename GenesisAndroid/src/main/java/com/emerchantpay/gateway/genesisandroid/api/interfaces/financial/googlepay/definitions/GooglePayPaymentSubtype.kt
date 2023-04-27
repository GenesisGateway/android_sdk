package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.googlepay.definitions

enum class GooglePayPaymentSubtype(private val value: String) {
    AUTHORIZE("authorize"),
    SALE("sale"),
    INIT_RECURRING_SALE("init_recurring_sale");

    override fun toString(): String {
        return value
    }
}