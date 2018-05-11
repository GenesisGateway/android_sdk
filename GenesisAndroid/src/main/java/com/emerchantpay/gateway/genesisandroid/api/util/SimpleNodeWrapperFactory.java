package com.emerchantpay.gateway.genesisandroid.api.util;

public class SimpleNodeWrapperFactory implements NodeWrapperFactory {
	public SimpleNodeWrapper create(String xml) {
		return SimpleNodeWrapper.parse(xml);
	}
}
