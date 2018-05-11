package com.emerchantpay.gateway.genesisandroid.api.internal.request;


import com.emerchantpay.gateway.genesisandroid.api.internal.GenesisValidator;
import com.emerchantpay.gateway.genesisandroid.api.util.Request;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

import java.util.ArrayList;

public class TransactionTypesRequest extends Request {

	private PaymentRequest parent;
	private CustomAttributesRequest customAttributes;
	private ArrayList<CustomAttributesRequest> customAttributesList = new ArrayList<CustomAttributesRequest>();

	// Genesis Validator
	private GenesisValidator validator;

	public TransactionTypesRequest() {
		super();
	}

	public void setValidator(GenesisValidator validator) {
		this.validator = validator;
	}

	public TransactionTypesRequest(PaymentRequest parent) {
		this.parent = parent;
	}

	public TransactionTypesRequest addTransaction(String transactionType) throws IllegalAccessException {
		validator.validateTransactionType(transactionType);
		customAttributes = new CustomAttributesRequest(this, transactionType);
		customAttributesList.add(customAttributes);
		return this;
	}

	public TransactionTypesRequest addParam(String key, String value) {
		this.customAttributes.addAttributeKey(key).addAttributeValue(value);
		return this;
	}

	@Override
	public String toXML() {
		return buildRequest("transaction_types").toXML();
	}

	@Override
	public String toQueryString(String root) {
		return buildRequest(root).toQueryString();
	}

	protected RequestBuilder buildRequest(String root) {

		RequestBuilder builder = new RequestBuilder(root);

		for (CustomAttributesRequest attribute: customAttributesList) {
			builder.addElement(null, attribute);
		}

		return builder;
	}

	public PaymentRequest done() {
		return parent;
	}
}
