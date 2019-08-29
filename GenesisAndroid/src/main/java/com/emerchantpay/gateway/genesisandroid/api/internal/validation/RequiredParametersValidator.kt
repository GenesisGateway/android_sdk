package com.emerchantpay.gateway.genesisandroid.api.internal.validation

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import java.util.*

class RequiredParametersValidator : GenesisValidator {
    private var parametersMap: HashMap<String, String>? = null
    private val missingParamsList = ArrayList<String>()

    // Genesis error handler
    override var error: GenesisError? = null
        set(value: GenesisError?) {
            super.error = value
        }

    // Missing params
    val isValidRequiredParams: Boolean?
        get() {
            val missingParams = StringBuilder()

            parametersMap!!.keys.forEach { key ->
                when {
                    parametersMap!![key] == null || parametersMap!![key]!!.isEmpty() -> missingParamsList.add(key)
                }
            }

            missingParamsList.indices.forEach { i ->
                when (i) {
                    0 -> missingParams.append(missingParamsList[0])
                    else -> missingParams.append(", " + missingParamsList[i])
                }
            }

            return when {
                missingParamsList.size > 1 -> {
                    error = GenesisError(ErrorMessages.REQUIRED_PARAMS, missingParams.toString())
                    false
                }
                !missingParamsList.isEmpty() -> {
                    error = GenesisError(ErrorMessages.REQUIRED_PARAM, missingParams.toString())
                    false
                }
                else -> true
            }
        }

    constructor() : super() {}

    constructor(parametersMap: HashMap<String, String>?) : super() {
        this.parametersMap = parametersMap
    }

    fun setParameters(parameters: HashMap<String, String>?): HashMap<String, String>? {
        this.parametersMap = parameters
        return this.parametersMap
    }
}
