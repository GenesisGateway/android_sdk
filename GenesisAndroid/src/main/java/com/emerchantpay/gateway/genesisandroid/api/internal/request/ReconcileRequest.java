package com.emerchantpay.gateway.genesisandroid.api.internal.request;


import com.emerchantpay.gateway.genesisandroid.api.util.Request;
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

import java.util.List;
import java.util.Map;

public class ReconcileRequest extends Request {

	private String uniqueId;

	public ReconcileRequest() {
		super();
	}

	public ReconcileRequest setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		return this;
	}

	@Override
	public String getTransactionType() {
		return "wpf_reconcile";
	}

	@Override
	public String toXML() {
		return buildRequest("wpf_reconcile").toXML();
	}

	@Override
	public String toQueryString(String root) {
		return buildRequest(root).toQueryString();
	}

	protected RequestBuilder buildRequest(String root) {

		return new RequestBuilder(root).addElement("unique_id", uniqueId);
	}

	public List<Map.Entry<String, Object>> getElements() {
		return buildRequest("").getElements();
	}
}
