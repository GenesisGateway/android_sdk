package com.emerchantpay.gateway.genesisandroid.api.constants

object TransactionTypes {
    // Standard Authorization
    const val AUTHORIZE = "authorize"

    // 3D-Secure Authorization
    const val AUTHORIZE_3D = "authorize3d"

    // Standard Sale
    const val SALE = "sale"

    // 3D-Secure Sale
    const val SALE_3D = "sale3d"

    // Standard initial recurring
    const val INIT_RECURRING_SALE = "init_recurring_sale"

    // 3D-based initial recurring
    const val INIT_RECURRING_SALE_3D = "init_recurring_sale3d"

    // Voucher-based payment
    val CASHU = "cashu"

    // Wallet-based payment
    const val EZEEWALLET = "ezeewallet"

    // Neteller
    const val NETELLER = "neteller"

    // POLi is Australia's most popular online real time debit payment system
    const val POLI = "poli"

    // WebMoney is a global settlement system and environment for online
    // business activities
    const val WEBMONEY = "webmoney"

    // Voucher-based payment
    const val PAYSAFECARD = "paysafecard"

    const val PPRO = "ppro"

    // Bank transfer payment, popular in Germany
    const val SOFORT = "sofort"

    val P24 = "P24"

    // Sepa Direct Debit
    const val SDD_SALE = "sdd_sale"
    const val SDD_INIT_RECURRING_SALE = "sdd_init_recurring_sale"

    //IDebit
    const val IDEBIT_PAYIN = "idebit_payin"

    //InstaDebit
    const val INSTADEBIT_PAYIN = "insta_debit_payin"

    //Citadel
    const val CITADEL_PAYIN = "citadel_payin"

    // PayPal Express Checkout
    const val PAYPAL_EXPRESS_CHECKOUT = "paypal_express"

    // Trustly
    const val TRUSTLY_SALE = "trustly_sale"
    const val TRUSTLY_WITHDRAWAL = "trustly_withdrawal"

    // Wechat
    const val WECHAT = "wechat"

    // PaySec
    const val PAYSEC = "paysec"

    // PaySec Payout
    const val PAYSEC_PAYOUT = "paysec_payout"

    // RPN
    const val RPN = "rpn_payment"

    // Gift Cards
    const val FASHIONCHEQUE = "fashioncheque"
    const val INTERSOLVE = "intersolve"
    const val TCS = "container_store"

    // Neosurf
    const val NEOSURF = "neosurf"

    // 	Klarna
    const val KLARNA_AUTHORIZE = "klarna_authorize"
}
