package com.emerchantpay.gateway.genesisandroid.api.internal.validation

import com.emerchantpay.gateway.genesisandroid.api.internal.request.KlarnaItemsRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress


class RequiredParameters {

    // Required params
    private val requiredParamsMap: HashMap<String, String> = HashMap<String, String>()

    fun getRequiredParametersForTransactionType(request: PaymentRequest, transactionType: String): HashMap<String, String> {

        val transactionParams: HashMap<String, String> = request.transactionTypes.customAttributes!!.paramsMap

        when (transactionType) {
            "ppro" -> {
                requiredParamsMap[productName] = when {
                    transactionParams[productName] != null -> transactionParams[productName].toString()
                    else -> ""
                }
                return requiredParamsMap
            }
            "citadelPayin" -> {
                requiredParamsMap[merchantCustomerId] = when {
                    transactionParams[merchantCustomerId] != null -> transactionParams[merchantCustomerId].toString()
                    else -> ""
                }
                return requiredParamsMap
            }
            "idebitPayin" -> {
                requiredParamsMap[customerAccountId] = when {
                    transactionParams[customerAccountId] != null -> transactionParams[customerAccountId].toString()
                    else -> ""
                }
                return requiredParamsMap
            }
            "instaDebitPayin" -> {
                requiredParamsMap[customerAccountId] = when {
                    transactionParams[customerAccountId] != null -> transactionParams[customerAccountId].toString()
                    else -> ""
                }
                return requiredParamsMap
            }
            "klarnaAuthorize" -> {
                requiredParamsMap[orderTaxAmount] = when {
                    transactionParams[orderTaxAmount] != null -> transactionParams[orderTaxAmount].toString()
                    else -> ""
                }
                requiredParamsMap[customerGender] = when {
                    transactionParams[orderTaxAmount] != null -> transactionParams[customerGender].toString()
                    else -> ""
                }
                return requiredParamsMap
            }
            else -> return HashMap()
        }
    }


    fun getRequiredParametersForKlarnaItem(klarnaItemsRequest: KlarnaItemsRequest): HashMap<String, String>? {
        klarnaItemsRequest.items.forEach { item ->
            requiredParamsMap[itemType] = item.itemType
            requiredParamsMap[itemName] = item.itemName
            requiredParamsMap[quantity] = item.quantity.toString()
            requiredParamsMap[unitPrice] = item.unitPrice.toString()
            requiredParamsMap[totalAmount] = item.totalAmount.toString()
        }

        return requiredParamsMap
    }


    fun getRequiredParametersForAddress(address: PaymentAddress): HashMap<String, String>? {
        requiredParamsMap[firstName] = address.firstName
        requiredParamsMap[address1] = address.address1
        requiredParamsMap[zipCode] = address.zipCode
        requiredParamsMap[country] = address.countryCode
        requiredParamsMap[state] = address.state

        return requiredParamsMap
    }

    fun getRequiredParametersForRequest(paymentRequest: PaymentRequest): HashMap<String, String>? {
        requiredParamsMap[transactionId] = paymentRequest.transactionId
        requiredParamsMap[amount] = paymentRequest.amount.toString()
        requiredParamsMap[currency] = paymentRequest.currency as String
        requiredParamsMap[transactionTypes] = paymentRequest.transactionTypes.transactionTypesList.toString()
        requiredParamsMap[returnSuccessUrl] = paymentRequest.returnSuccessUrl
        requiredParamsMap[returnCancelUrl] = paymentRequest.returnCancelUrl
        requiredParamsMap[customerEmail] = paymentRequest.customerEmail
        when {
            paymentRequest.customerEmail != null
                    && !paymentRequest.customerEmail.isEmpty()
                    && (paymentRequest.consumerId == null || paymentRequest.consumerId!!.isEmpty()) ->
                requiredParamsMap[consumerId] = paymentRequest.consumerId as String
        }
        requiredParamsMap[notificationUrl] = paymentRequest.notificationUrl as String
        requiredParamsMap[customerPhone] = paymentRequest.customerPhone
        requiredParamsMap[billingAddress] = paymentRequest.getBillingAddress().toXML()

        return requiredParamsMap
    }

    companion object {
        const val transactionId = "transaction_id"
        const val amount = "amount"
        const val currency = "currency"
        const val transactionTypes = "transaction_types"
        const val transactionTypeName = "transaction_type_name"
        const val returnSuccessUrl = "return_success_url"
        const val returnFailureUrl = "return_failure_url"
        const val returnCancelUrl = "return_cancel_url"
        const val customerEmail = "customer_email"
        const val consumerId = "consumer_id"
        const val customerPhone = "customer_phone"
        const val billingAddress = "billing_address"
        const val shippingAddress = "shipping_address"
        const val notificationUrl = "notification_url"
        const val usage = "usage"
        const val paymentDescription = "payment_description"
        const val riskParams = "riskParams"
        const val dynamicDescriptorParams = "dynamic_descriptor_params"
        const val lifetime = "lifetime"
        const val firstName = "firstname"
        const val lastName = "lastname"
        const val address1 = "address1"
        const val address2 = "address2"
        const val zipCode = "zip_code"
        const val city = "city"
        const val country = "country"
        const val state = "state"
        const val customerAccountId = "customer_account_id"
        const val sourceWalletId = "source_wallet_id"
        const val merchantCustomerId = "merchant_customer_id"
        const val productName = "product_name"
        const val productCategory = "product_category"
        const val cardType = "card_type"
        const val redeemType = "redeem_type"

        // Klarna
        const val orderTaxAmount = "order_tax_amount"
        const val customerGender = "customer_gender"
        const val items = "items"
        const val itemType = "item_type"
        const val itemName = "name"
        const val quantity = "quantity"
        const val unitPrice = "unitPrice"
        const val totalAmount = "total_amount"
    }
}
