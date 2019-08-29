package com.emerchantpay.gateway.genesisandroid.api.internal

import com.emerchantpay.gateway.genesisandroid.api.models.Transaction

import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper

class TransactionResult<T>(node: NodeWrapper, klass: Class<T>) {

    val transaction: Transaction = Transaction(node)

}
