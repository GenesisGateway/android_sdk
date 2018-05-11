package com.emerchantpay.gateway.genesisandroid.api.internal;

import com.emerchantpay.gateway.genesisandroid.api.models.Transaction;

import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper;

public class TransactionGateway {

    Configuration configuration;
    NodeWrapper response;


    public TransactionGateway(Configuration configuration, NodeWrapper response) {
        super();

        this.configuration = configuration;
        this.response = response;
    }

    public TransactionResult<Transaction> getRequest() {
        return new TransactionResult(response, Transaction.class);
    }
}
