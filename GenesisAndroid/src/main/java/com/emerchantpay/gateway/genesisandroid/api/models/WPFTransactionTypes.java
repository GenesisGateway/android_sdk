package com.emerchantpay.gateway.genesisandroid.api.models;

import java.util.Date;

public class WPFTransactionTypes {

    private String name;
    private String bin;
    private String tail;

    // Default variable
    private Boolean def;

    private Date expirationDate;

    //WPF Transaction types
    public static String authorize = "authorize";
    public static String authorize3d = "authorize3d";
    public static String sale = "sale";
    public static String sale3d = "sale3d";
    public static String initRecurringSale = "init_recurring_sale";
    public static String initRecurringSale3d = "init_recurring_sale3d";
    public static String ezeewallet = "ezeewallet";
    public static String sofort = "sofort";
    public static String cashu = "cashu";
    public static String paysafecard = "paysafecard";
    public static String ppro = "ppro";
    public static String neteller = "neteller";
    public static String poli = "poli";
    public static String p24 = "p24";
    public static String citadelPayin = "citadel_payin";
    public static String idebitPayin = "idebit_payin";
    public static String instaDebitPayin = "insta_debit_payin";
    public static String paypalExpress = "paypal_express";
    public static String webmoney = "webmoney";
    public static String sddSale = "sdd_sale";
    public static String sddInitRecurringSale = "sdd_init_recurring_sale";
    public static String trustlySale = "trustly_sale";
    public static String trustlyWithdrawal = "trustly_withdrawal";
    public static String wechat = "wechat";
    public static String rpnPayment = "rpn_payment";
    public static String paysec = "paysec";
    public static String paysecPayout = "paysec_payout";
    public static String intersolve = "intersolve";
    public static String fashioncheque = "fashioncheque";
    public static String containerStore = "container_store";
    public static String neosurf = "neosurf";
    public static String klarnaAuthorize = "klarna_authorize";

    public WPFTransactionTypes() {

    }

    public WPFTransactionTypes(String name) {
        super();

        this.name = name;
    }

    public String getTransactionName() {
        return name;
    }
}
