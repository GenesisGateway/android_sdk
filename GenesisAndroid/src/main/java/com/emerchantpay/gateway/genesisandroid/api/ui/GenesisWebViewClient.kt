package com.emerchantpay.gateway.genesisandroid.api.ui

import android.graphics.Bitmap
import android.net.http.SslError
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.util.WebViewUtil

open class GenesisWebViewClient(// WebView Activity
        private val webViewActivity: GenesisWebViewActivity, // Progress Bar
        private val progressBar: ProgressBar) : WebViewClient() {

    override fun onPageStarted(view: WebView, url: String, icon: Bitmap?) {
        super.onPageStarted(view, url, icon)

        // Show Progress bar
        progressBar.visibility = View.VISIBLE

        // Destroy webview activity
        val result = WebViewUtil.getResultIntent(url)
        val resultCode = result.first
        val resultIntent = result.second

        when {
            result.first != null -> {
                resultCode?.let { webViewActivity.setResult(it, resultIntent) }
                stopLoading()
            }
        }
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        view.title?.let { webViewActivity.setActionBarTitle(it) }

        // Dismiss Progress bar
        progressBar.visibility = View.GONE
    }

    @Deprecated("Deprecated in Java")
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
