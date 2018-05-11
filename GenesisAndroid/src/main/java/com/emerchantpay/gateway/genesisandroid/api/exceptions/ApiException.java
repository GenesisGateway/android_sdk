package com.emerchantpay.gateway.genesisandroid.api.exceptions;


import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorCodes;

public class ApiException extends GenesisException {

	private static final long serialVersionUID = 1L;

	String description;

	public ApiException(Integer code, String message, Throwable cause) {
		super(code, message, cause);

		description = ErrorCodes.getErrorDescription(code);
		message = description;
	}

	public ApiException() {
		super();
	}
}
