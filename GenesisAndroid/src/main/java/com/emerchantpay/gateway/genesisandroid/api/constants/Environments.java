package com.emerchantpay.gateway.genesisandroid.api.constants;

import java.io.Serializable;

public class Environments implements Serializable {

	private String environmentName;

	// Production Environments
	public static Environments PRODUCTION = new Environments("gate");

	// Staging Environments
	public static Environments STAGING = new Environments("staging.gate");

	public Environments(String environmentName) {

		this.environmentName = environmentName;
	}

	public String getEnvironmentName() {
		return this.environmentName;
	}

	public String toString() {
		return getEnvironmentName();
	}
}
