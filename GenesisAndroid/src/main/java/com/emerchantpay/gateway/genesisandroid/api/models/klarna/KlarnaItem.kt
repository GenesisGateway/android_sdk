package com.emerchantpay.gateway.genesisandroid.api.models.klarna

import com.emerchantpay.gateway.genesisandroid.api.constants.KlarnaItemTypes
import com.emerchantpay.gateway.genesisandroid.api.util.Request
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

import java.math.BigDecimal
import java.math.MathContext

class KlarnaItem(val itemName: String // Descriptive item name.
                 , val itemType: String //Order line type
                 , val quantity: Int? //Non-negative. The item quantity.
                 , unitPrice: BigDecimal, internal var totalAmount: BigDecimal? //Includes tax and discount. Must match (quantity unit price) - to- tal discount amount divided by quan- tity. (max value: 100000000)
) : Request() {
    var unitPrice: BigDecimal? = null
        private set//Minor units. Includes tax, excludes discount. (max value: 100000000)
    private var reference: String? = null //Article number, SKU or similar.
    private var taxRate: BigDecimal? = null  //Non-negative. In percent, two implicit decimals. I.e 2500 = 25.00 percent.
    private var totalDiscountAmount: BigDecimal? = null //Non-negative minor units. Includes tax.
    internal var totalTaxAmount: BigDecimal? = null //Must be within 1 of total amount - to- tal amount 10000 / (10000 + tax rate). Negative when type is discount.
    private var imageUrl: String? = null //URL to an image that can be later embedded in communications between Klarna and the customer. (max 1024 characters)
    private var productUrl: String? = null //URL to an image that can be later embedded in communications between Klarna and the customer. (max 1024 characters)
    private var quantityUnit: String? = null //Unit used to describe the quantity, e.g. kg, pcs... If defined has to be 1-8 char- acters
    private var productIdentifiers: ProductIdentifiers? = null //List with product identifiers
    private val brand: String? = null //The product’s brand name as generally recognized by consumers. If no brand is available for a product, do not sup- ply any value.
    private val categoryPath: String? = null //The product’s category path as used in the merchant’s webshop. Include the full and most detailed category and separate the segments with ’ ¿ ’
    private val globalTradeItemNumber: String? = null //The product’s Global Trade Item Number (GTIN). Common types of GTIN are EAN, ISBN or UPC. Ex- clude dashes and spaces, where possi- ble
    private val manufacturerPartNumber: String? = null //The product’s Manufacturer Part Number (MPN), which - together with the brand - uniquely identifies a product. Only submit MPNs assigned by a manufacturer and use the most specific MPN possible
    private var merchantData: MerchantData? = null //List with merchant data

    private val isDiscount: Boolean?
        get() = itemType == KlarnaItemTypes.DISCOUNT

    init {
        this.unitPrice = unitPrice
    }

    fun setReference(reference: String): KlarnaItem {
        this.reference = reference
        return this
    }

    fun setTaxRate(taxRate: BigDecimal): KlarnaItem {
        this.taxRate = taxRate
        return this
    }

    fun setTotalDiscountAmount(totalDiscountAmount: BigDecimal): KlarnaItem {
        this.totalDiscountAmount = totalDiscountAmount
        return this
    }

    fun setTotalTaxAmount(totalTaxAmount: BigDecimal): KlarnaItem {
        this.totalTaxAmount = totalTaxAmount
        return this
    }

    fun setImageUrl(imageUrl: String): KlarnaItem {
        this.imageUrl = imageUrl
        return this
    }

    fun setProductUrl(productUrl: String): KlarnaItem {
        this.productUrl = productUrl
        return this
    }

    fun setQuantityUnit(quantityUnit: String): KlarnaItem {
        this.quantityUnit = quantityUnit
        return this
    }

    fun setProductIdentifiers(productIdentifiers: ProductIdentifiers): ProductIdentifiers? {
        this.productIdentifiers = productIdentifiers
        return this.productIdentifiers
    }

    fun setMerchantData(merchantData: MerchantData): MerchantData? {
        this.merchantData = merchantData
        return this.merchantData
    }

    override fun toXML(): String {
        return buildRequest("item").toXML()
    }

    override fun toQueryString(root: String): String {
        return buildRequest(root).toQueryString()
    }

    protected fun buildRequest(root: String): RequestBuilder {
        val builder = RequestBuilder(root)

        // Convert amounts
        convertAmounts()

        // Product Identifiers
        if (productIdentifiers != null) {
            builder.addElement(productIdentifiers!!.toXML())
        }

        // Merchant Data
        if (merchantData != null) {
            merchantData!!.toXML()?.let { builder.addElement(it) }
        }

        builder.addElement("item_type", itemType)
                .addElement("quantity", quantity!!)
                .addElement("unit_price", unitPrice!!)
                .addElement("total_amount", totalAmount!!)
                .addElement("reference", reference!!)
                .addElement("name", itemName)
                .addElement("tax_rate", taxRate!!)
                .addElement("total_discount_amount", totalDiscountAmount!!)
                .addElement("total_tax_amount", totalTaxAmount!!)
                .addElement("image_url", imageUrl!!)
                .addElement("product_url", productUrl!!)
                .addElement("quantity_unit", quantityUnit!!)

        return builder
    }

    protected fun convertAmounts() {
        val multiplyExp = BigDecimal(Math.pow(10.0, 2.0), MathContext.DECIMAL64)

        // Unit price
        when {
            unitPrice != null && unitPrice!!.toDouble() > 0 -> {
                unitPrice = unitPrice!!.multiply(multiplyExp)
                unitPrice = BigDecimal(unitPrice!!.toInt())
            }
            else -> unitPrice = BigDecimal(0)
        }

        // Total discount
        totalDiscountAmount = when {
            totalDiscountAmount != null && totalDiscountAmount!!.toDouble() > 0 -> totalDiscountAmount!!.multiply(multiplyExp)
            else -> BigDecimal(0)
        }

        // Total amount convertion
        when {
            quantity!! > 0 && unitPrice != null && unitPrice!!.toDouble() > 0 -> {
                var totalAmountInt: Int? = 0

                if (isDiscount!! && totalDiscountAmount != null
                        && totalDiscountAmount!!.toDouble() > 0) {
                    totalAmountInt = quantity!! * unitPrice!!.toInt() - totalDiscountAmount!!.toInt()
                } else {
                    totalDiscountAmount = BigDecimal(0)
                    totalAmountInt = quantity!! * unitPrice!!.toInt()
                }

                totalAmount = BigDecimal(totalAmountInt)
            }

            // Tax rate convertion

            // Total tax amount convertion
        }

        // Tax rate convertion
        taxRate = when {
            taxRate != null && taxRate!!.toDouble() > 0 -> taxRate!!.multiply(multiplyExp)
            else -> BigDecimal(0)
        }

        // Total tax amount convertion
        when {
            totalTaxAmount != null && totalTaxAmount!!.toDouble() > 0 -> {
                totalTaxAmount!!.multiply(multiplyExp)
                val totalTaxAmountInt = (totalAmount!!.toInt() - totalAmount!!.toInt() * 10000) / (taxRate!!.toInt() + 10000)
                totalTaxAmount = BigDecimal(totalTaxAmountInt)
                totalTaxAmount = BigDecimal(Math.ceil(totalTaxAmount!!.toDouble()))
            }
        }
    }

    fun getTotalAmount(): BigDecimal? {
        return (when {
            totalAmount != null && totalAmount!!.toDouble() > 0 -> totalAmount
            else -> BigDecimal(0)
        }) as BigDecimal
    }

    fun getTotalTaxAmount(): BigDecimal? {
        return when {
            totalTaxAmount != null && totalTaxAmount!!.toDouble() > 0 -> totalTaxAmount
            else -> BigDecimal(0)
        }
    }
}
