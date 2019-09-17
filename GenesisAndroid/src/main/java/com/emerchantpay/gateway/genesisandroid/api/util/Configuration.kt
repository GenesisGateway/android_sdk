package com.emerchantpay.gateway.genesisandroid.api.util

import android.util.Base64
import com.emerchantpay.gateway.genesisandroid.api.constants.Endpoints
import com.emerchantpay.gateway.genesisandroid.api.constants.Environments
import java.io.Serializable
import java.util.*

class Configuration(var username: String?, var password: String?, val environment: Environments, var endpoint: Endpoints, var language: Locale?) : Serializable {
        var token: String? = null
    var action: String? = null
    var tokenEnabled: Boolean? = true
    var wpfEnabled: Boolean? = false
    var isDebugModeEnabled: Boolean? = false

    val baseUrl: String
        get() = if (tokenEnabled == true) {
            ("https://" + environment.environmentName + "." + endpoint.endpointName + "/" + action
                    + "/" + token)
        } else {

            if (wpfEnabled == true) {
                ("https://" + environment.environmentName.replace("gate", "wpf") + "."
                        + endpoint.endpointName + "/" + action)
            } else {
                ("https://" + environment.environmentName + "." + endpoint.endpointName + "/"
                        + action)
            }
        }

    fun setDebugMode(enabled: Boolean?) {
        this.isDebugModeEnabled = enabled
    }

    fun encodeCredentialsToBase64(): String {
        val user_pass = "$username:$password"
        val encodedBytes = Base64.encode(user_pass.toByteArray(),
                Base64.NO_WRAP)

        return String(encodedBytes)
    }
}
