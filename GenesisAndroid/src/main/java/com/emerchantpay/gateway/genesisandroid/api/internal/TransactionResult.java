package com.emerchantpay.gateway.genesisandroid.api.internal;

import com.emerchantpay.gateway.genesisandroid.api.models.Transaction;

import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper;

public class TransactionResult<T>{

    private Transaction transaction;

    public TransactionResult(NodeWrapper node, Class<T> klass) {
        this.transaction = new Transaction(node);
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
