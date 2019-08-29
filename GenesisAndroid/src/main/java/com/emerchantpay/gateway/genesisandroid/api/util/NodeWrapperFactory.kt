package com.emerchantpay.gateway.genesisandroid.api.util

interface NodeWrapperFactory {

    fun create(xml: String): SimpleNodeWrapper

    companion object {
        val instance = SimpleNodeWrapperFactory() as NodeWrapperFactory
    }
}
