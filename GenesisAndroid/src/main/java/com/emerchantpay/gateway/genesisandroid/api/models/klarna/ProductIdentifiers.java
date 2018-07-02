package com.emerchantpay.gateway.genesisandroid.api.models.klarna;

import com.emerchantpay.gateway.genesisandroid.api.util.Request;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

import java.util.List;
import java.util.Map;

public class ProductIdentifiers extends Request {

    private RequestBuilder builder =  new RequestBuilder("product_identifiers");

    private String brand; //The product’s brand name as generally recognized by consumers. If no brand is available for a product, do not sup- ply any value.
    private String categoryPath; //The product’s category path as used in the merchant’s webshop. Include the full and most detailed category and separate the segments with ’ ¿ ’
    private String globalTradeItemNumber; //The product’s Global Trade Item Number (GTIN). Common types of GTIN are EAN, ISBN or UPC. Ex- clude dashes and spaces, where possi- ble
    private String manufacturerPartNumber; //The product’s Manufacturer Part Number (MPN), which - together with the brand - uniquely identifies a product. Only submit MPNs assigned by a manufacturer and use the most specific MPN possible

    public ProductIdentifiers setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public ProductIdentifiers setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
        return this;
    }

    public ProductIdentifiers setGlobalTradeItemNumber(String globalTradeItemNumber) {
        this.globalTradeItemNumber = globalTradeItemNumber;
        return this;
    }

    public ProductIdentifiers setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
        return this;
    }

    public ProductIdentifiers addProductIdentifier(String key, String value) {
        builder.addElement(key, value);
        return this;
    }

    public ProductIdentifiers addProductIdentifiers(Map<String, String> productIdentifiersMap) {
        for (Map.Entry<String, String> entry : productIdentifiersMap.entrySet()) {
            builder.addElement(entry.getKey(), entry.getValue());
        }

        return this;
    }

    @Override
    public String toXML() {
        return buildRequest().toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest().toQueryString();
    }

    protected RequestBuilder buildRequest() {
        return builder.addElement("brand", brand)
                .addElement("category_path", categoryPath)
                .addElement("global_trade_number", globalTradeItemNumber)
                .addElement("manufacturer_part_number", manufacturerPartNumber);
    }

    public List<Map.Entry<String,Object>> getProductIdentifiersList() {
        return builder.getElements();
    }
}
