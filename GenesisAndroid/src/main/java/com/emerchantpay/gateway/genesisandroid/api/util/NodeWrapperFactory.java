package com.emerchantpay.gateway.genesisandroid.api.util;

public interface NodeWrapperFactory {
	NodeWrapperFactory instance = (NodeWrapperFactory) new SimpleNodeWrapperFactory();

	SimpleNodeWrapper create(String xml);
}
