package com.emerchantpay.gateway.genesisandroid.api.internal.response

import com.emerchantpay.gateway.genesisandroid.api.constants.TransactionStates
import com.emerchantpay.gateway.genesisandroid.api.internal.GenesisClient
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.models.Transaction

open class Response(// Genesis client
        private val client: GenesisClient) {

    // Get Redirect Url
    val redirectUrl: String?
        get() = transaction?.redirectUrl

    // Get Unique Id
    val uniqueId: String?
        get() = transaction?.uniqueId

    // Get status
    val status: String?
        get() = transaction?.status

    // Get Status code
    val code: Int?
        get() = transaction?.code

    // Get Technical message
    val technicalMessage: String
        get() {
            val technicalMessage = transaction?.technicalMessage

            return when {
                technicalMessage == null || technicalMessage.isEmpty() -> {
                    val paymentTransaction = transaction?.paymentTransactions?.get(0)
                    paymentTransaction?.findString("technical_message")
                }
                else -> technicalMessage
            } as String
        }

    // Get Message
    val message: String?
        get() = transaction?.message

    val transaction: Transaction?
        get() = client.transaction?.request?.transaction

    // Get success transaction
    val isSuccess: Boolean?
        get() = !(status == TransactionStates.ERROR || status == TransactionStates.DECLINED)

    // Get Error object
    val error: GenesisError?
        get() = when {
            (!isSuccess!!)!! -> message?.let { GenesisError(code, it, technicalMessage) }
            else -> null
        }
}