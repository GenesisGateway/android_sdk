package com.emerchantpay.gateway.genesisandroid.api.models

open class GenesisError {

    var code: Int? = null
        private set
    var technicalMessage: String? = null
        private set
    var message: String? = null
        private set

    constructor() : super() {}

    constructor(code: Int?, message: String, technicalMessage: String) : super() {

        this.code = code
        this.message = message
        this.technicalMessage = technicalMessage
    }

    constructor(code: Int?, technicalMessage: String) : super() {

        this.code = code
        this.technicalMessage = technicalMessage
    }

    constructor(message: String, technicalMessage: String) : super() {

        this.message = message
        this.technicalMessage = technicalMessage
    }

    constructor(message: String) : super() {

        this.message = message
    }
}
