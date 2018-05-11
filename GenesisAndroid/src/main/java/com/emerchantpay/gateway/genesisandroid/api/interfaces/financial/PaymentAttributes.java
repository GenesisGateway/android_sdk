package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial;

import com.emerchantpay.gateway.genesisandroid.api.models.Currency;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

import java.math.BigDecimal;

public interface PaymentAttributes {

    RequestBuilder requestBuilder = new RequestBuilder("");

    // Payment Params
    PaymentAttributes setAmount(BigDecimal amount);

    BigDecimal getAmount();

    PaymentAttributes setCurrency(String currency);

    String getCurrency();

    default RequestBuilder buildPaymentParams() {

        BigDecimal convertedAmount = null;

        if (getAmount() != null && getCurrency() != null) {

            Currency curr = new Currency();

            curr.setAmountToExponent(getAmount(), getCurrency());
            convertedAmount = curr.getAmount();
        }

        requestBuilder.addElement("amount", convertedAmount)
                .addElement("currency", getCurrency());

        return requestBuilder;
    }
}
