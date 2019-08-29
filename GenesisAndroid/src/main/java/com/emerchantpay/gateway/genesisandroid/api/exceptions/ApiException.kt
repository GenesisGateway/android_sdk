package com.emerchantpay.gateway.genesisandroid.api.exceptions


import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorCodes

class ApiException : GenesisException {

    override lateinit var message: String
    private lateinit var description: String

    constructor(code: Int?, message: String, cause: Throwable) : super(code, message, cause) {
        description = ErrorCodes.getErrorDescription(code!!)
        this.message = description
    }

    constructor() : super() {}

    companion object {

        private const val serialVersionUID = 1L
    }
}
