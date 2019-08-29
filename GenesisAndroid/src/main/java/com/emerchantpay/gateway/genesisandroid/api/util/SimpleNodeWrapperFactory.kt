package com.emerchantpay.gateway.genesisandroid.api.util

class SimpleNodeWrapperFactory : NodeWrapperFactory {
    override fun create(xml: String): SimpleNodeWrapper {
        return SimpleNodeWrapper.parse(xml)
    }
}
