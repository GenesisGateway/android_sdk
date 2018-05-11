package com.emerchantpay.gateway.genesisandroid.api.internal.request;

import com.emerchantpay.gateway.genesisandroid.api.util.Request;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilderWithAttribute;

import java.util.ArrayList;

public class CustomAttributesRequest extends Request {

	private TransactionTypesRequest parent;

	private String transactionType;

	private ArrayList<String> keyList = new ArrayList<String>();
	private ArrayList<String> valueList = new ArrayList<String>();

	public CustomAttributesRequest() {
		super();
	}

	public CustomAttributesRequest(TransactionTypesRequest parent, String transactionType) {
		this.parent = parent;
		this.transactionType = transactionType;
	}

	public CustomAttributesRequest addAttributeKey(String key) {
		keyList.add(key);
		return this;
	}

	public CustomAttributesRequest addAttributeValue(String value) {
		valueList.add(value);
		return this;
	}

	@Override
	public String toXML() {
		return buildRequest("transaction_type").toXML();
	}

	@Override
	public String toQueryString(String root) {
		return buildRequest(root).toQueryString();
	}

	protected RequestBuilderWithAttribute buildRequest(String root) {

		RequestBuilderWithAttribute builder;

		builder = new RequestBuilderWithAttribute(root, transactionType);

		for (int i = 0; i < keyList.size(); i++) {
			builder.addElement(keyList.get(i), valueList.get(i));
		}

		return builder;
	}

	public TransactionTypesRequest done() {
		return parent;
	}
}
