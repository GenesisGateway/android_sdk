package com.emerchantpay.gateway.genesisandroid.api.exceptions

open class GenesisException : RuntimeException {

    constructor(code: Int?, message: String, cause: Throwable) : super(message, cause) {}

    constructor(message: String?, cause: Throwable?) : super(message, cause) {}

    constructor(message: String) : super(message) {}

    constructor() : super() {}

    companion object {
        private const val serialVersionUID = 1L
    }
}
