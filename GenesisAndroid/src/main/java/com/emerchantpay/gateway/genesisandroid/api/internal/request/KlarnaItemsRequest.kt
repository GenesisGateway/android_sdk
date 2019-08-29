package com.emerchantpay.gateway.genesisandroid.api.internal.request

import com.emerchantpay.gateway.genesisandroid.api.models.klarna.KlarnaItem
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder
import java.math.BigDecimal
import java.util.*

open class KlarnaItemsRequest : PaymentRequest {

    internal var items = ArrayList<KlarnaItem>()

    val totalAmounts: BigDecimal
        get() {
            var totalAmounts = BigDecimal(0)

            items.forEach { item ->
                totalAmounts = totalAmounts.add(item.totalAmount?.toDouble()?.let { BigDecimal(it) })
            }

            return totalAmounts
        }

    val totalTaxAmounts: BigDecimal
        get() {
            var totalTaxAmounts = BigDecimal(0)

            items.forEach { item ->
                totalTaxAmounts = totalTaxAmounts.add(item.totalTaxAmount?.toDouble()?.let { BigDecimal(it) })
            }

            return totalTaxAmounts
        }

    constructor(klarnaItem: KlarnaItem) {
        this.items.add(klarnaItem)
    }

    constructor(klarnaItemsList: ArrayList<KlarnaItem>) {
        this.items = klarnaItemsList
    }

    override fun toXML(): String {
        return buildRequest("items")!!.toXML()
    }

    override fun toQueryString(root: String): String {
        return buildRequest(root)!!.toQueryString()
    }

    override fun buildRequest(root: String): RequestBuilder? {

        val builder = RequestBuilder(root)

        items.forEach { item ->
            builder.addElement(item)
        }

        return builder
    }
}
