package com.emerchantpay.gateway.genesisandroid.api.util;

import android.util.Base64;

import com.emerchantpay.gateway.genesisandroid.api.constants.Endpoints;
import com.emerchantpay.gateway.genesisandroid.api.constants.Environments;

import java.io.Serializable;
import java.util.Locale;

public class Configuration implements Serializable {

	private Environments environment;
	private Endpoints endpoint;
	private String username;
	private String password;
	private String token;
	private String action;
	private Boolean tokenEnabled = true;
	private Boolean wpf = false;
	private Boolean enabledDebugMode = false;
	private Locale language;

	public Configuration(String username, String password, Environments environment, Endpoints endpoint, Locale language) {

		this.username = username;
		this.password = password;
		this.environment = environment;
		this.endpoint = endpoint;
		this.language = language;
	}

	public void setDebugMode(Boolean enabled) {
		this.enabledDebugMode = enabled;
	}

	public Boolean isDebugModeEnabled() {
		return this.enabledDebugMode;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {

		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {

		return password;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {

		return token;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setWpfEnabled(Boolean wpf) {
		this.wpf = wpf;
	}

	public Boolean getWpfEnabled() {
		return wpf;
	}

	public void setTokenEnabled(Boolean tokenEnabled) {
		this.tokenEnabled = tokenEnabled;
	}

	public Boolean getTokenEnabled() {
		return tokenEnabled;
	}

	public void setLanguage(Locale language) {
		this.language = language;
	}

	public Locale getLanguage() {
		return language;
	}

	public String getBaseUrl() {

		if (getTokenEnabled() == true) {
			return "https://" + environment.getEnvironmentName() + "." + endpoint.getEndpointName() + "/" + getAction()
					+ "/" + token;
		} else {

			if (getWpfEnabled() == true) {
				return "https://" + environment.getEnvironmentName().replace("gate", "wpf") + "."
						+ endpoint.getEndpointName() + "/" + getAction();
			} else {
				return "https://" + environment.getEnvironmentName() + "." + endpoint.getEndpointName() + "/"
						+ getAction();
			}
		}
	}

	public String encodeCredentialsToBase64() {
		String user_pass = username + ":" + password;
		byte[] encodedBytes = Base64.encode(user_pass.getBytes(),
				Base64.NO_WRAP);
		String encodedString = new String(encodedBytes);

		return encodedString;
	}

	public Environments getEnvironment() {
		return environment;
	}

	public Endpoints getEndpoint() {
		return endpoint;
	}
}
