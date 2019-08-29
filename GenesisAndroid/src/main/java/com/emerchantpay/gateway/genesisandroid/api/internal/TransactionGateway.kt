package com.emerchantpay.gateway.genesisandroid.api.internal

import com.emerchantpay.gateway.genesisandroid.api.models.Transaction

import com.emerchantpay.gateway.genesisandroid.api.util.Configuration
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper

open class TransactionGateway(internal var configuration: Configuration?, internal var response: NodeWrapper) {

    val request: TransactionResult<Transaction>
        get() = TransactionResult(response, Transaction::class.java)
}
