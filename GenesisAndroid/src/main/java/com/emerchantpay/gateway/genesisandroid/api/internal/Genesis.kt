package com.emerchantpay.gateway.genesisandroid.api.internal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.ReconcileRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.response.Response
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebViewActivity
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration
import java.net.URL


open class Genesis {

    // Application context
    private var context: Context? = null

    // Configuration
    private var configuration: Configuration? = null

    // WPF request
    var paymentRequest: PaymentRequest? = null

    // WPF Reconcile request
    var wpfReconcileRequest: ReconcileRequest? = null

    // Genesis client
    private var client: GenesisClient? = null

    // Genesis error handler
    var error: GenesisError? = null
        get() =
            when {
                isValidData!! -> response?.error
                else -> paymentRequest?.validator?.error
            }

    val response: Response?
        get() = client?.let { Response(it) }

    val isValidData: Boolean?
        get() = paymentRequest != null && paymentRequest?.isValidData!!

    constructor(context: Context, configuration: Configuration, paymentRequest: PaymentRequest) : super() {

        this.context = context
        this.configuration = configuration
        this.paymentRequest = paymentRequest
    }

    constructor(context: Context, configuration: Configuration, wpfReconcile: ReconcileRequest) : super() {

        this.context = context
        this.configuration = configuration
        this.wpfReconcileRequest = wpfReconcile
    }

    fun push() {
        // Create GatewayClient
        // Start activity
        when {
            paymentRequest != null -> client = context?.let {
                configuration?.let { it1 ->
                    GenesisClient(it, it1, paymentRequest!!)
                }
            }
            wpfReconcileRequest != null -> client = context?.let {
                configuration?.let { it1 ->
                    GenesisClient(it, it1, wpfReconcileRequest!!)
                }
            }
        }

        // Set Debug mode
        client!!.debugMode(configuration!!.isDebugModeEnabled)

        // Execute Payment Request
        client!!.execute()

        // Get response
        val response = response

        val redirectUrl = response?.redirectUrl
        val uniqueId = response?.uniqueId

        when {
            redirectUrl != null -> {
                // Start activity
                val intent = Intent(context, GenesisWebViewActivity::class.java)
                intent.putExtra(IntentExtras.EXTRA_REDIRECT_URL, redirectUrl)
                intent.putExtra(IntentExtras.EXTRA_UNIQUE_ID, uniqueId)
                intent.putExtra(IntentExtras.EXTRA_CONFIGURATION, configuration)
                (context as Activity).startActivityForResult(intent, 1)
            }
        }
    }

    fun loadRedirectUrl(url: URL?) {
        when {
            url != null -> {
                // Start activity
                val intent = Intent(context, GenesisWebViewActivity::class.java)
                intent.putExtra(IntentExtras.EXTRA_REDIRECT_URL, url.toString())
                intent.putExtra(IntentExtras.EXTRA_CONFIGURATION, configuration)
                (context as Activity).startActivityForResult(intent, 1)
            }
        }
    }

    // Check Network connection status
    fun isConnected(context: Context): Boolean? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        when {
            !isConnected -> error = GenesisError(ErrorMessages.CONNECTION_ERROR)
        }

        return isConnected
    }
}
