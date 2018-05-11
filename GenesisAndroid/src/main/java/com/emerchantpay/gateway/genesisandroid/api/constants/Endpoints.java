package com.emerchantpay.gateway.genesisandroid.api.constants;

import java.io.Serializable;

public class Endpoints implements Serializable {

	private String endpointName;

	// Domain for E-ComProcessing's Genesis instance
	public static Endpoints ECOMPROCESSING = new Endpoints("e-comprocessing.net");

	// Domain for Emerchantpay's Genesis instance
	public static Endpoints EMERCHANTPAY = new Endpoints("emerchantpay.net");

	public Endpoints(String endpointName) {
		this.endpointName = endpointName;
	}

	public String getEndpointName() {
		return this.endpointName;
	}

	public String toString() {
		return getEndpointName();
	}
}
