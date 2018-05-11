package com.emerchantpay.gateway.genesisandroid.api.internal.response;

import com.emerchantpay.gateway.genesisandroid.api.constants.TransactionStates;
import com.emerchantpay.gateway.genesisandroid.api.internal.GenesisClient;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.models.Transaction;
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper;

public class Response {

    // Genesis client
    private GenesisClient client;

    public Response(GenesisClient client) {
        super();

        this.client = client;
    }

    // Get Redirect Url
    public String getRedirectUrl() {
        return getTransaction().getRedirectUrl();
    }

    // Get Unique Id
    public String getUniqueId() {
        return getTransaction().getUniqueId();
    }

    // Get status
    public String getStatus() {
        return getTransaction().getStatus();
    }

    // Get Status code
    public Integer getCode() {
        return getTransaction().getCode();
    }

    // Get Technical message
    public String getTechnicalMessage() {
        String technicalMessage = getTransaction().getTechnicalMessage();

        if (technicalMessage == null || technicalMessage.isEmpty()) {
            NodeWrapper paymentTransaction = getTransaction().getPaymentTransactions().get(0);
            return paymentTransaction.findString("technical_message");
        } else {
            return technicalMessage;
        }
    }

    // Get Message
    public String getMessage() {
        return getTransaction().getMessage();
    }

    public Transaction getTransaction() {
        return client.getTransaction().getRequest().getTransaction();
    }

    // Get success transaction
    public Boolean isSuccess() {
        if (getStatus().equals(TransactionStates.ERROR) ||
                getStatus().equals(TransactionStates.DECLINED)) {
            return false;
        } else {
            return true;
        }
    }

    // Get Error object
    public GenesisError getError() {
        if (!isSuccess()) {
            return new GenesisError(getCode(), getMessage(), getTechnicalMessage());
        } else {
            return null;
        }
    }
}