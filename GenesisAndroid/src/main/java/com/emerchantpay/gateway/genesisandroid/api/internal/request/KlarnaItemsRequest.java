package com.emerchantpay.gateway.genesisandroid.api.internal.request;

import com.emerchantpay.gateway.genesisandroid.api.models.klarna.KlarnaItem;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;

public class KlarnaItemsRequest extends PaymentRequest {

    private ArrayList<KlarnaItem> klarnaItemsList = new ArrayList<KlarnaItem>();

    public KlarnaItemsRequest(KlarnaItem klarnaItem) {
        this.klarnaItemsList.add(klarnaItem);
    }

    public KlarnaItemsRequest(ArrayList<KlarnaItem> klarnaItemsList) {
        this.klarnaItemsList = klarnaItemsList;
    }

    @Override
    public String toXML() {
        return buildRequest("items").toXML();
    }

    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {

        RequestBuilder builder = new RequestBuilder(root);

        for (KlarnaItem item : klarnaItemsList) {
            builder.addElement(item);
        }

        return builder;
    }

    public BigDecimal getTotalAmounts() {
        BigDecimal totalAmounts = new BigDecimal(0);

        for (KlarnaItem item : klarnaItemsList) {
            totalAmounts = totalAmounts.add(new BigDecimal(item.getTotalAmount().doubleValue()));
        }

        return totalAmounts;
    }

    public BigDecimal getTotalTaxAmounts() {
        BigDecimal totalTaxAmounts = new BigDecimal(0);

        for (KlarnaItem item : klarnaItemsList) {
            totalTaxAmounts = totalTaxAmounts.add(new BigDecimal(item.getTotalTaxAmount().doubleValue()));
        }

        return totalTaxAmounts;
    }

    public ArrayList<KlarnaItem> getItems() {
        return klarnaItemsList;
    }
}
