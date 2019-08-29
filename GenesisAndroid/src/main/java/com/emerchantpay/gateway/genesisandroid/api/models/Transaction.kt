package com.emerchantpay.gateway.genesisandroid.api.models


import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper

import java.math.BigDecimal

open class Transaction(node: NodeWrapper?) {

    var document: String
        private set
    var status: String
        private set
    var transcationType: String? = ""
        private set
    var uniqueId: String? = ""
        private set
    var transactionId: String? = ""
        private set
    var code: Int?
        private set
    var message: String? = ""
        private set
    var technicalMessage: String? = ""
        private set
    var descriptor: String? = ""
        private set
    var amount: BigDecimal? = null
        private set
    var currency: String?
        private set
    var redirectUrl: String? = ""
        private set
    var mode: String? = ""
        private set
    var timestamp: String? = ""
        private set
    var sentToAcquirer: Boolean?
        private set
    var authorizationCode: String? = ""
        private set
    var responseCode: String? = ""
        private set
    var gaming: String? = ""
        private set
    var moto: String? = ""
        private set
    var partialAproval: String? = ""
        private set
    var avsResponseCode: String? = ""
        private set
    var avsResponseText: String? = ""
        private set
    var dynamicDescriptorParams: List<String>
        private set
    private var customParam: String? = null
        private set
    var referenceTransactionUId: String? = ""
        private set
    var arn: String? = ""
        private set
    var cardBrand: String? = ""
        private set
    var cardNumber: String? = ""
        private set
    var invalidTypesForAmount: String? = ""
        private set
    var splitPayment: String? = ""
        private set
    var leftoverAmount: Int?
        private set
    var paymentResponses: Map<String, String>
        private set
    var paymentTransactions: List<NodeWrapper>
        private set
    var consumerId: String? = ""
        private set


    init {

        this.document = node?.toString()!!
        this.status = node?.findString("status")!!
        this.transcationType = node?.findString("transaction_type")
        this.uniqueId = node?.findString("unique_id")
        this.transactionId = node?.findString("transaction_id")
        this.code = node?.findInteger("code")!!
        this.message = node?.findString("message")
        this.technicalMessage = node?.findString("technical_message")
        this.descriptor = node?.findString("descriptor")
        this.amount = node?.findBigDecimal("amount")
        this.currency = node.findString("currency")
        this.redirectUrl = node?.findString("redirect_url")
        this.mode = node.findString("mode")
        this.timestamp = node.findString("timestamp")
        this.sentToAcquirer = node.findBoolean("sent_to_acquirer")
        this.authorizationCode = node.findString("authorization_code")
        this.responseCode = node.findString("response_code")
        this.gaming = node.findString("gaming")
        this.moto = node.findString("moto")
        this.partialAproval = node.findString("partial_approval")
        this.avsResponseCode = node.findString("avs_response_code")
        this.avsResponseText = node.findString("avs_response_text")
        this.dynamicDescriptorParams = node.findAllStrings("dynamic_descriptor_params")
        this.referenceTransactionUId = node.findString("reference_transaction_unique_id")
        this.arn = node.findString("arn")
        this.cardBrand = node.findString("card_brand")
        this.cardNumber = node.findString("card_number")
        this.invalidTypesForAmount = node.findString("invalid_transactions_for_amount")
        this.splitPayment = node.findString("split_payment")
        this.leftoverAmount = node.findInteger("leftover_amount")
        this.paymentResponses = node.formParameters
        this.paymentTransactions = node.findAll("payment_transaction")
        this.consumerId = node.findString("consumer_id")

        if (this.amount != null && this.currency != null) {

            val curr = Currency()

            curr.setExponentToAmount(this.amount!!, this.currency!!)
            this.amount = curr.amount
        }
    }

    fun getCustomParam(node: NodeWrapper, key: String): String? {
        this.customParam = node.findString(key)
        return customParam
    }
}