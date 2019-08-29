package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface MpiAttributes {

    // Mpi Params
    fun setCavv(cavv: String): MpiAttributes {
        requestBuilder.addElement("cavv", cavv)
        return this
    }

    fun setEci(eci: String): MpiAttributes {
        requestBuilder.addElement("eci", eci)
        return this
    }

    fun setXid(xid: String): MpiAttributes {
        requestBuilder.addElement("xid", xid)
        return this
    }

    fun buildMpiParams(): RequestBuilder {
        return requestBuilder
    }

    companion object {

        val requestBuilder = RequestBuilder("")
    }
}