package com.emerchantpay.gateway.genesisandroid.api.internal.request


import com.emerchantpay.gateway.genesisandroid.api.util.Request
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

class ReconcileRequest : Request() {

    private var uniqueId: String? = null

    val elements: MutableList<Map.Entry<String, Any>>?
        get() = buildRequest("")?.elements

    fun setUniqueId(uniqueId: String): ReconcileRequest {
        this.uniqueId = uniqueId
        return this
    }

    override fun toXML(): String? {
        return buildRequest("wpf_reconcile")?.toXML()
    }

    override fun toQueryString(root: String): String {
        return buildRequest(root)?.toQueryString().toString()
    }

    private fun buildRequest(root: String): RequestBuilder? {

        return uniqueId?.let { RequestBuilder(root).addElement("unique_id", it) }
    }

    override fun getTransactionType(): String? {
        return "wpf_reconcile"
    }
}
