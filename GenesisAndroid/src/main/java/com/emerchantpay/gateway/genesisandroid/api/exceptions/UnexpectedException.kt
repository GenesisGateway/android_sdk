package com.emerchantpay.gateway.genesisandroid.api.exceptions


class UnexpectedException : GenesisException {

    constructor(message: String) : super(message) {}

    constructor(message: String?, cause: Throwable?) : super(message, cause) {}

    companion object {

        private const val serialVersionUID = 1L
    }
}
