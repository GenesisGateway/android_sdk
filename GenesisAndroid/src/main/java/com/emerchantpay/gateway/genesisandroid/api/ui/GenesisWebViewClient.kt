package com.emerchantpay.gateway.genesisandroid.api.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras
import com.emerchantpay.gateway.genesisandroid.api.constants.URLConstants
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError

open class GenesisWebViewClient(// WebView Activity
        private val webViewActivity: GenesisWebViewActivity, // Progress Bar
        private val progressBar: ProgressBar) : WebViewClient() {

    override fun onPageStarted(view: WebView, url: String, icon: Bitmap?) {
        super.onPageStarted(view, url, icon)

        // Show Progress bar
        progressBar.visibility = View.VISIBLE

        // Destroy webview activity
        val returnIntent = Intent()
        var resultCode: Int? = null

        when {
            url.contains(URLConstants.FAILURE_URL) -> {
                returnIntent.putExtra(IntentExtras.EXTRA_RESULT, "failure")
                resultCode = Activity.RESULT_OK
            }
            url.contains(URLConstants.CANCEL_URL) -> {
                returnIntent.putExtra("cancel", "cancel")
                resultCode = Activity.RESULT_CANCELED
            }
            url.contains(URLConstants.SUCCESS_URL) -> {
                returnIntent.putExtra("success", "success")
                resultCode = Activity.RESULT_OK
            }
        }

        when {
            resultCode != null -> {
                resultCode?.let { webViewActivity.setResult(it, returnIntent) }
                stopLoading()
            }
        }
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        webViewActivity.setActionBarTitle(view.title)

        // Dismiss Progress bar
        progressBar.visibility = View.GONE
    }

    override fun onReceivedError(view: WebView, errorCode: Int, description: String,
                                 failingUrl: String) {
        finishActivity(view, errorCode, description)
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        handler.cancel()
        finishActivity(view, error.primaryError, ErrorMessages.SSL_ERROR)
    }

    private fun stopLoading() {
        progressBar.visibility = View.GONE
        webViewActivity.finish()
    }

    private fun finishActivity(view: WebView, errorCode: Int, description: String) {
        view.stopLoading()
        webViewActivity.finishWithResult(GenesisError(errorCode, description))
    }
}
