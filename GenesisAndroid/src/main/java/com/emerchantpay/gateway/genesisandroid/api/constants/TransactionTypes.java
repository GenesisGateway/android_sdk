package com.emerchantpay.gateway.genesisandroid.api.constants;

public class TransactionTypes {

	// Standard Authorization
	public static String AUTHORIZE = "authorize";

	// 3D-Secure Authorization
	public static String AUTHORIZE_3D = "authorize3d";

	// Standard Sale
	public static String SALE = "sale";

	// 3D-Secure Sale
	public static String SALE_3D = "sale3d";

	// Standard initial recurring
	public static String INIT_RECURRING_SALE = "init_recurring_sale";

	// 3D-based initial recurring
	public static String INIT_RECURRING_SALE_3D = "init_recurring_sale3d";

	// Voucher-based payment
	public static String CASHU = "cashu";

	// Wallet-based payment
	public static String EZEEWALLET = "ezeewallet";

	// Neteller
	public static String NETELLER = "neteller";

	// POLi is Australia's most popular online real time debit payment system
	public static String POLI = "poli";

	// WebMoney is a global settlement system and environment for online
	// business activities
	public static String WEBMONEY = "webmoney";

	// Voucher-based payment
	public static String PAYSAFECARD = "paysafecard";

	public static String PPRO = "ppro";

	// Bank transfer payment, popular in Germany
	public static String SOFORT = "sofort";
	
	public static String P24 = "P24";

	// Sepa Direct Debit
	public static String SDD_SALE = "sdd_sale";
	public static String SDD_INIT_RECURRING_SALE = "sdd_init_recurring_sale";

	//IDebit
	public static String IDEBIT_PAYIN = "idebit_payin";

	//InstaDebit
	public static String INSTADEBIT_PAYIN = "insta_debit_payin";
  
	//Citadel
	public static String CITADEL_PAYIN = "citadel_payin";

	// PayPal Express Checkout
	public static String PAYPAL_EXPRESS_CHECKOUT = "paypal_express";

	// Trustly
	public static String TRUSTLY_SALE = "trustly_sale";
	public static String TRUSTLY_WITHDRAWAL = "trustly_withdrawal";

	// Wechat
	public static String WECHAT = "wechat";

	// PaySec
	public static String PAYSEC = "paysec";

	// PaySec Payout
	public static String PAYSEC_PAYOUT	 = "paysec_payout";

	// RPN
	public static String RPN = "rpn_payment";

	// Gift Cards
	public static String FASHIONCHEQUE = "fashioncheque";
	public static String INTERSOLVE = "intersolve";
	public static String TCS = "container_store";

	// Neosurf
	public static String NEOSURF = "neosurf";

	// 	Klarna
	public static String KLARNA_AUTHORIZE = "klarna_authorize";
}
