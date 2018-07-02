package com.emerchantpay.gateway.genesisandroid.api.internal.validation;

import com.emerchantpay.gateway.genesisandroid.api.internal.request.KlarnaItemsRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;
import com.emerchantpay.gateway.genesisandroid.api.models.klarna.KlarnaItem;

import java.util.HashMap;

public class RequiredParameters {
    public static String transactionId = "transaction_id";
    public static String amount = "amount";
    public static String currency = "currency";
    public static String transactionTypes = "transaction_types";
    public static String transactionTypeName = "transaction_type_name";
    public static String returnSuccessUrl = "return_success_url";
    public static String returnFailureUrl = "return_failure_url";
    public static String returnCancelUrl = "return_cancel_url";
    public static String customerEmail = "customer_email";
    public static String customerPhone = "customer_phone";
    public static String billingAddress = "billing_address";
    public static String shippingAddress = "shipping_address";
    public static String notificationUrl = "notification_url";
    public static String usage = "usage";
    public static String paymentDescription = "payment_description";
    public static String riskParams = "riskParams";
    public static String dynamicDescriptorParams = "dynamic_descriptor_params";
    public static String lifetime = "lifetime";
    public static String firstName = "firstname";
    public static String lastName = "lastname";
    public static String address1 = "address1";
    public static String address2 = "address2";
    public static String zipCode = "zip_code";
    public static String city = "city";
    public static String country = "country";
    public static String state = "state";
    public static String customerAccountId = "customer_account_id";
    public static String sourceWalletId = "source_wallet_id";
    public static String merchantCustomerId = "merchant_customer_id";
    public static String productName = "product_name";
    public static String productCategory = "product_category";
    public static String cardType = "card_type";
    public static String redeemType = "redeem_type";

    // Klarna
    public static String orderTaxAmount = "order_tax_amount";
    public static String customerGender = "customer_gender";
    public static String items = "items";
    public static String itemType = "item_type";
    public static String itemName = "name";
    public static String quantity = "quantity";
    public static String unitPrice = "unitPrice";
    public static String totalAmount = "total_amount";

    // Required params
    private HashMap<String, String> requiredParamsMap = new HashMap<String, String>();

    public HashMap<String, String> getRequiredParametersForTransactionType(PaymentRequest request, String transactionType) {

        HashMap<String, String> transactionParams = request.getTransactionTypes().getCustomAttributes().getParamsMap();

        switch (transactionType) {
            case "ppro":
                requiredParamsMap.put(productName, transactionParams.get(productName));
                return requiredParamsMap;
            case "citadelPayin":
                requiredParamsMap.put(transactionType, transactionParams.get(merchantCustomerId));
                return requiredParamsMap;
            case "idebitPayin":
                requiredParamsMap.put(transactionType, transactionParams.get(customerAccountId));
                return requiredParamsMap;
            case "instaDebitPayin":
                requiredParamsMap.put(transactionType, transactionParams.get(customerAccountId));
                return requiredParamsMap;
            case "klarnaAuthorize":
                requiredParamsMap.put(transactionType, transactionParams.get(orderTaxAmount));
                requiredParamsMap.put(transactionType, transactionParams.get(customerGender));
                return requiredParamsMap;
            default:
                return new HashMap<String, String>();
        }
    }


    public HashMap<String, String> getRequiredParametersForKlarnaItem(KlarnaItemsRequest klarnaItemsRequest) {

        for (KlarnaItem item : klarnaItemsRequest.getItems()) {
            requiredParamsMap.put(itemType, item.getItemType());
            requiredParamsMap.put(itemName, item.getItemName());
            requiredParamsMap.put(quantity, String.valueOf(item.getQuantity()));
            requiredParamsMap.put(unitPrice, String.valueOf(item.getUnitPrice()));
            requiredParamsMap.put(totalAmount, String.valueOf(item.getTotalAmount()));
        }

        return requiredParamsMap;
    }


    public HashMap<String, String> getRequiredParametersForAddress(PaymentAddress address) {
        requiredParamsMap.put(firstName, address.getFirstName());
        requiredParamsMap.put(address1, address.getAddress1());
        requiredParamsMap.put(zipCode, address.getZipCode());
        requiredParamsMap.put(country, address.getCountryCode());
        requiredParamsMap.put(state, address.getState());

        return requiredParamsMap;
    }

    public HashMap<String, String> getRequiredParametersForRequest(PaymentRequest paymentRequest) {
        requiredParamsMap.put(transactionId, paymentRequest.getTransactionId());
        requiredParamsMap.put(amount, String.valueOf(paymentRequest.getAmount()));
        requiredParamsMap.put(currency, paymentRequest.getCurrency());
        requiredParamsMap.put(transactionTypes, paymentRequest.getTransactionTypes().getTransactionTypesList().toString());
        requiredParamsMap.put(returnSuccessUrl, paymentRequest.getReturnSuccessUrl());
        requiredParamsMap.put(returnCancelUrl, paymentRequest.getReturnCancelUrl());
        requiredParamsMap.put(customerEmail, paymentRequest.getCustomerEmail());
        requiredParamsMap.put(notificationUrl, paymentRequest.getNotificationUrl());
        requiredParamsMap.put(customerPhone, paymentRequest.getCustomerPhone());
        requiredParamsMap.put(billingAddress, paymentRequest.getBillingAddress().toXML());

        return requiredParamsMap;
    }
}
