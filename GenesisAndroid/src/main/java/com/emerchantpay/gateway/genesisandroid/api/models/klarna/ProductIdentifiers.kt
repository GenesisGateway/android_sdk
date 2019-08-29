package com.emerchantpay.gateway.genesisandroid.api.models.klarna

import com.emerchantpay.gateway.genesisandroid.api.util.Request
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

class ProductIdentifiers : Request() {

    private val builder = RequestBuilder("product_identifiers")

    private var brand: String? = null //The product’s brand name as generally recognized by consumers. If no brand is available for a product, do not sup- ply any value.
    private var categoryPath: String? = null //The product’s category path as used in the merchant’s webshop. Include the full and most detailed category and separate the segments with ’ ¿ ’
    private var globalTradeItemNumber: String? = null //The product’s Global Trade Item Number (GTIN). Common types of GTIN are EAN, ISBN or UPC. Ex- clude dashes and spaces, where possi- ble
    private var manufacturerPartNumber: String? = null //The product’s Manufacturer Part Number (MPN), which - together with the brand - uniquely identifies a product. Only submit MPNs assigned by a manufacturer and use the most specific MPN possible

    val productIdentifiersList: ArrayList<Map.Entry<String, Any>>?
        get() = builder.elements as ArrayList<Map.Entry<String, Any>>?

    fun setBrand(brand: String): ProductIdentifiers {
        this.brand = brand
        return this
    }

    fun setCategoryPath(categoryPath: String): ProductIdentifiers {
        this.categoryPath = categoryPath
        return this
    }

    fun setGlobalTradeItemNumber(globalTradeItemNumber: String): ProductIdentifiers {
        this.globalTradeItemNumber = globalTradeItemNumber
        return this
    }

    fun setManufacturerPartNumber(manufacturerPartNumber: String): ProductIdentifiers {
        this.manufacturerPartNumber = manufacturerPartNumber
        return this
    }

    fun addProductIdentifier(key: String, value: String): ProductIdentifiers {
        builder.addElement(key, value)
        return this
    }

    fun addProductIdentifiers(productIdentifiersMap: Map<String, String>): ProductIdentifiers {
        for ((key, value) in productIdentifiersMap) {
            builder.addElement(key, value)
        }

        return this
    }

    override fun toXML(): String {
        return buildRequest().toXML()
    }

    override fun toQueryString(root: String): String {
        return buildRequest().toQueryString()
    }

    protected fun buildRequest(): RequestBuilder {
        return builder.addElement("brand", brand!!)
                .addElement("category_path", categoryPath!!)
                .addElement("global_trade_number", globalTradeItemNumber!!)
                .addElement("manufacturer_part_number", manufacturerPartNumber!!)
    }
}
