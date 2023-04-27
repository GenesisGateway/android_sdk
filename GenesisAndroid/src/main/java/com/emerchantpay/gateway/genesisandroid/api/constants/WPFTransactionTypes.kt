package com.emerchantpay.gateway.genesisandroid.api.constants

import java.util.*

enum class WPFTransactionTypes(val value: String) {
    // Standard Authorization
    AUTHORIZE("authorize"),

    // 3D-Secure Authorization
    AUTHORIZE3D("authorize3d"),

    // Standard Sale
    SALE("sale"),

    // 3D-Secure Sale
    SALE3D("sale3d"),

    // Card verification without any financial impact
    ACCOUNT_VERIFICATION("account_verification"),

    // Payment using credit or debit cards connected to a consumer's Google account
    GOOGLE_PAY("google_pay"),

    // Wallet-based payment
    EZEEWALLET("ezeewallet"),

    // Bank transfer payment, popular in Germany
    SOFORT("sofort"),

    // Voucher-based payment
    CASHU("cashu"),

    // Voucher-based payment
    PAYSAFECARD("paysafecard"),

    // Online Banking ePayment method
    POST_FINANCE("post_finance"),

    // Supports payments with EPS, TeleIngreso, SafetyPay, TrustPay, Przelewy24,
    // iDEAL, QIWI, GiroPay, Mr. Cash and MyBank
    PPRO("ppro"),

    // Wallet-based payment
    NETELLER("neteller"),

    // POLi is Australia's most popular online real time debit payment system
    // Payment by bank account for customers with an Australian or New Zealand bank account
    POLI("poli"),

    // Payment by bank account for customers with a Polish bank account
    P24("p24"),

    // Online Banking ePayment method
    IDEBIT_PAYIN("idebit_payin"),

    // Online Banking ePayment method
    INSTA_DEBIT_PAYIN("insta_debit_payin"),

    // Express payment by PayPal balance
    PAYPAL_EXPRESS("paypal_express"),

    // WebMoney is a global settlement system and environment for online business activities
    // Bank transfer payment, popular in Russian Federation
    WEBMONEY("webmoney"),

    // SEPA Direct Debit Sale
    SDD_SALE("sdd_sale"),

    // SEPA Direct Debit init recurring
    SDD_INIT_RECURRING_SALE("sdd_init_recurring_sale"),

    // Solution for Online Banking ePayments
    TRUSTLY_SALE("trustly_sale"),

    // Allows you to pay with your online bank
    TRUSTLY_WITHDRAWAL("trustly_withdrawal"),

    // Online Banking ePayment method
    WECHAT("wechat"),

    // APMs Invoice Transaction
    INVOICE("invoice"),

    //Citadel
    CITADEL_PAYIN("citadel_payin"),

    // PaySec
    PAYSEC("paysec"),

    // PaySec Payout
    PAYSEC_PAYOUT("paysec_payout"),

    // RPN
    RPN_PAYMENT("rpn_payment"),

    // Gift Cards
    FASHIONCHEQUE("fashioncheque"),
    INTERSOLVE("intersolve"),
    TCS("container_store"),

    // Neosurf
    NEOSURF("neosurf"),

    // Klarna
    KLARNA_AUTHORIZE("klarna_authorize"),

    // Bancontact is a local Belgian debit card scheme
    BANCONTACT("bcmc"),

    // BitPay payments are a crypto currency method where merchants are requesting
    BITPAY_PAYOUT("bitpay_payout"),
    BITPAY_SALE("bitpay_sale"),

    // Direct PPRO transaction
    IDEAL("ideal"),

    // InstantTransfer is a payment method in Germany
    INSTANT_TRANSFER("instant_transfer"),

    // Online Banking is an oBeP-style alternative payment method that allows you to pay directly
    ONLINE_BANKING_PAYIN("online_banking"),

    // PayU is a payment method for Czech Republic and Poland
    PAYU("payu"),

    //  My Bank is an overlay banking system
    MY_BANK("my_bank"),

    // Zimpler is a Swedish payment method.
    ZIMPLER("zimpler"),

    // South American Payments
    ARGENCARD("argencard"),
    AURA("aura"),
    BALOTO("baloto"),
    BANCOMER("bancomer"),
    BANCO_DE_OCCIDENTE("banco_de_occidente"),
    BANCO_DO_BRASIL("banco_do_brasil"),
    BOLETO("boleto"),
    BRADESCO("bradesco"),
    CABAL("cabal"),
    CENCOSUD("cencosud"),
    DAVIVIENDA("davivienda"),
    EFECTY("efecty"),
    ELO("elo"),
    ITAU("itau"),
    MULTIBANCO("multibanco"),
    NARANJA("naranja"),
    NATIVA("nativa"),
    OXXO("oxxo"),
    PAGO_FACIL("pago_facil"),
    PSE("pse"),
    RAPI_PAGO("rapi_pago"),
    REDPAGOS("redpagos"),
    SANTANDER("santander"),
    SANTANDER_CASH("santander_cash"),
    TARJETA_SHOPPING("tarjeta_shopping"),

    // ePayment methods popular in India
    UPI("upi"),
    ONLINE_BANKING("online_banking"),
    NETBANKING("netbanking");

    override fun toString(): String {
        return name.lowercase(Locale.ROOT)
    }
}