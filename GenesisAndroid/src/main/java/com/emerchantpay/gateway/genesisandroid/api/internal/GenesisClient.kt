package com.emerchantpay.gateway.genesisandroid.api.internal

import android.content.Context
import com.emerchantpay.gateway.genesisandroid.api.constants.Endpoints
import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorCodes
import com.emerchantpay.gateway.genesisandroid.api.constants.SharedPrefConstants
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.ReconcileRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.RetrieveConsumerRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.response.Response
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.network.HttpAsyncTask
import com.emerchantpay.gateway.genesisandroid.api.util.*
import java.util.concurrent.ExecutionException

open class GenesisClient : Request {

    private var context: Context? = null
    private var configuration: Configuration? = null
    private var genesisRequest: Request? = null

    constructor(context: Context?, configuration: Configuration?, request: Request?) : super() {
        this.context = context
        this.configuration = configuration
        this.genesisRequest = request
    }

    // Execute
    private var http: HttpAsyncTask? = null
    var response: NodeWrapper? = null
        private set

    val transaction: TransactionGateway?
        get() = response?.let { TransactionGateway(configuration, it) }

    fun debugMode(enabled: Boolean?): GenesisClient {
        configuration?.setDebugMode(enabled)
        return this
    }

    fun changeRequest(request: Request?): GenesisClient {
        this.genesisRequest = request
        return this
    }

    fun execute(): Request {
        when (genesisRequest!!.getTransactionType()) {
            "wpf_payment" -> {
                configuration?.wpfEnabled = true
                configuration?.tokenEnabled = false

                when {
                    configuration?.language != null -> configuration?.action = configuration?.language.toString() + "/wpf"
                    else -> configuration?.action = "wpf"
                }
            }
            "wpf_reconcile" -> {
                configuration?.wpfEnabled = true
                configuration?.tokenEnabled = false
                configuration?.action = "wpf/reconcile"
            }
            "reconcile" -> {
                configuration?.wpfEnabled = false
                configuration?.tokenEnabled = true
                configuration?.action = "reconcile"
            }
            "reconcile_by_date" -> {
                configuration?.wpfEnabled = false
                configuration?.tokenEnabled = true
                configuration?.action = "reconcile/by_date"
            }
            "blacklist" -> {
                configuration?.wpfEnabled = false
                configuration?.tokenEnabled = false
                configuration?.action = "blacklists"
            }
            "chargeback" -> {
                configuration?.wpfEnabled = false
                configuration?.tokenEnabled = false
                configuration?.action = "chargebacks"
            }
            "chargeback_by_date" -> {
                configuration?.wpfEnabled = false
                configuration?.tokenEnabled = false
                configuration?.action = "chargebacks/by_date"
            }
            "reports_fraud" -> {
                configuration?.wpfEnabled = false
                configuration?.tokenEnabled = false
                configuration?.action = "fraud_reports"
            }
            "reports_fraud_by_date" -> {
                configuration?.wpfEnabled = false
                configuration?.tokenEnabled = false
                configuration?.action = "fraud_reports/by_date"
            }
            "retrieval_requests" -> {
                configuration?.wpfEnabled = false
                configuration?.tokenEnabled = false
                configuration?.action = "retrieval_requests"
            }
            "retrieval_requests_by_date" -> {
                configuration?.wpfEnabled = false
                configuration?.tokenEnabled = false
                configuration?.action = "retrieval_requests/by_date"
            }
            "retrieve_consumer_request" -> {
                configuration?.wpfEnabled = false
                configuration?.tokenEnabled = false
                configuration?.action = ""
            }
            else -> {
                configuration?.wpfEnabled = false
                configuration?.tokenEnabled = true
                configuration?.action = "process"
            }
        }

        http = HttpAsyncTask(configuration)

        try {
            var paymentRequest: PaymentRequest? = null
            response = when (genesisRequest) {
                is ReconcileRequest -> http?.execute(configuration?.baseUrl, genesisRequest as? ReconcileRequest)?.get()
                else -> {
                    paymentRequest = genesisRequest as? PaymentRequest
                    http?.execute(configuration?.baseUrl, paymentRequest)?.get()
                }
            }

            // Retrieve and store consumer id
            val sharedPrefs = GenesisSharedPreferences()
            val result = this.transaction?.request
            var consumerId = result?.transaction?.consumerId
            when {
                Response(this).isSuccess!!
                        && consumerId.isNullOrEmpty()
                        && !paymentRequest?.getCustomerEmail().isNullOrEmpty() -> {
                    consumerId = retrieveConsumerIdFromGenesisGateway(paymentRequest?.getCustomerEmail())
                    when {
                        !consumerId.isNullOrEmpty() ->
                            sharedPrefs.putString(context, SharedPrefConstants.CONSUMER_ID,
                                    KeyStoreUtil(context).encryptData(consumerId))
                    }
                }
                sharedPrefs?.getString(context, SharedPrefConstants.CONSUMER_ID).isNullOrEmpty()
                        && response?.isSuccess == true
                        && consumerId != null
                        && consumerId.isNotEmpty() -> sharedPrefs.putString(context, SharedPrefConstants.CONSUMER_ID,
                        KeyStoreUtil(context).encryptData(result?.transaction?.consumerId))
            }
        } catch (e: InterruptedException) {
            GenesisError(ErrorCodes.SYSTEM_ERROR.code, e.message.toString())
        } catch (e: ExecutionException) {
            GenesisError(ErrorCodes.SYSTEM_ERROR.code, e.message.toString())
        }

        return this
    }

    private fun retrieveConsumerIdFromGenesisGateway(email: String?): String? {
        http = HttpAsyncTask(configuration)

        val retrieveConsumerRequest = RetrieveConsumerRequest(context, email)
        when (configuration?.endpoint) {
            Endpoints.EMERCHANTPAY -> {
                configuration?.endpoint = Endpoints.RETRIEVE_CONSUMER_EMERCHANTPAY
            }
            Endpoints.ECOMPROCESSING -> {
                configuration?.endpoint = Endpoints.RETRIEVE_CONSUMER_ECOMPROCESSING
            }
        }
        configuration?.wpfEnabled = false
        configuration?.tokenEnabled = false
        configuration?.action = ""
        response = http!!.execute(configuration?.baseUrl, retrieveConsumerRequest).get()
        val result = this.transaction?.request
        return result?.transaction?.consumerId
    }
}
