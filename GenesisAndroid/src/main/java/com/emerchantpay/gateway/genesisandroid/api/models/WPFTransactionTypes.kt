package com.emerchantpay.gateway.genesisandroid.api.models

import java.util.*

class WPFTransactionTypes {

    private var transactionName: String = ""
    private val bin: String? = null
    private val tail: String? = null

    // Default const valiable
    private val def: Boolean? = null

    private val expirationDate: Date? = null

    constructor() {

    }

    constructor(name: String) : super() {
        this.transactionName = name
    }

    companion object {

        //WPF Transaction types
        const val authorize = "authorize"
        const val authorize3d = "authorize3d"
        const val sale = "sale"
        const val sale3d = "sale3d"
        const val initRecurringSale = "init_recurring_sale"
        const val initRecurringSale3d = "init_recurring_sale3d"
        const val ezeewallet = "ezeewallet"
        const val sofort = "sofort"
        const val cashu = "cashu"
        const val paysafecard = "paysafecard"
        const val ppro = "ppro"
        const val neteller = "neteller"
        const val poli = "poli"
        const val p24 = "p24"
        const val citadelPayin = "citadel_payin"
        const val idebitPayin = "idebit_payin"
        const val instaDebitPayin = "insta_debit_payin"
        const val paypalExpress = "paypal_express"
        const val webmoney = "webmoney"
        const val sddSale = "sdd_sale"
        const val sddInitRecurringSale = "sdd_init_recurring_sale"
        const val trustlySale = "trustly_sale"
        const val trustlyWithdrawal = "trustly_withdrawal"
        const val wechat = "wechat"
        const val rpnPayment = "rpn_payment"
        const val paysec = "paysec"
        const val paysecPayout = "paysec_payout"
        const val intersolve = "intersolve"
        const val fashioncheque = "fashioncheque"
        const val containerStore = "container_store"
        const val neosurf = "neosurf"
        const val klarnaAuthorize = "klarna_authorize"
    }
}
