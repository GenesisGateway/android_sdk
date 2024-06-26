package com.emerchantpay.gateway.genesisandroid.api.ui

import android.app.Activity
import android.os.Message
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar

import com.emerchantpay.gateway.genesisandroid.api.util.WebViewUtil

open class GenesisWebChromeClient(// WebView Activity
        private val webViewActivity: GenesisWebViewActivity, // Progress Bar
        private val progressBar: ProgressBar) : WebChromeClient() {

    override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
        val newWebView = GenesisWebView(webViewActivity.applicationContext, progressBar)
        newWebView.init(webViewActivity)
        webViewActivity.pushNewWebView(newWebView)
        (resultMsg.obj as WebView.WebViewTransport).webView = newWebView
        resultMsg.sendToTarget()

        // Show Progress bar
        progressBar.visibility = View.VISIBLE

        val url = view.url

        val result = url?.let { WebViewUtil.getResultIntent(it) }
        val resultCode = result?.first

        when {
            resultCode != null -> {
                stopLoading(view)
                resultCode?.let { finishActivity(webViewActivity, it) }
            }
        }

        return true
    }

    override fun onCloseWindow(window: WebView) {
        webViewActivity.popCurrentWebView()

        // Dismiss Progress bar
        progressBar.visibility = View.GONE
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        when {
            newProgress < 100 -> {
                webViewActivity.setProgress(newProgress)
                webViewActivity.setProgressBarVisibility(true)
            }
            else -> {
                webViewActivity.setProgressBarVisibility(false)

                // Dismiss Progress bar
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun stopLoading(view: WebView) {
        progressBar.visibility = View.GONE
        view.stopLoading()
    }

    private fun finishActivity(activity: Activity, resultCode: Int) {
        activity.setResult(resultCode)
        activity.finish()
    }
}
