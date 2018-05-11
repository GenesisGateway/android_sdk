package com.emerchantpay.gateway.genesisandroid.api.util;


import com.emerchantpay.gateway.genesisandroid.api.interfaces.AddressAttributes;
import com.emerchantpay.gateway.genesisandroid.api.interfaces.BaseAttributes;

import java.io.Serializable;

/**
 * Abstract class for fluent interface request builders.
 */
public abstract class Request implements BaseAttributes, AddressAttributes, Serializable {

	public String toXML() {
		throw new UnsupportedOperationException();
	}

	public String toQueryString(String parent) {
		throw new UnsupportedOperationException();
	}

	public String toQueryString() {
		throw new UnsupportedOperationException();
	}

	public String getKind() {
		return null;
	}

	protected String buildXMLElement(Object element) {
		return RequestBuilder.buildXMLElement(element);
	}

	protected String buildXMLElement(String name, Object element) {
		return RequestBuilder.buildXMLElement(name, element);
	}

	public Request getRequest() {
		return this;
	}

	public String getTransactionType() {
		return null;
	}
}
