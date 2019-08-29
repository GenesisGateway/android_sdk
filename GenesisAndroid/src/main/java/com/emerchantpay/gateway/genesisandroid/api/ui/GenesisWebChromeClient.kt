package com.emerchantpay.gateway.genesisandroid.api.ui

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar

import com.emerchantpay.gateway.genesisandroid.api.constants.URLConstants

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

        val returnIntent = Intent()

        when  {
            url.contains(URLConstants.FAILURE_URL.toLowerCase()) -> {
                webViewActivity.executeReconcile()

                stopLoading(view)
            }
            url.contains(URLConstants.CANCEL_URL.toLowerCase()) -> {
                stopLoading(view)

                returnIntent.putExtra("cancel", "cancel")
                finishActivity(webViewActivity)
            }
            url.contains(URLConstants.SUCCESS_URL.toLowerCase()) -> {
                returnIntent.putExtra("success", "success")
                finishActivity(webViewActivity)

                stopLoading(view)
            }
            url.contains(URLConstants.DASHBOARD.toLowerCase()) -> {
                returnIntent.putExtra("dashboard", "dashboard")
                finishActivity(webViewActivity)

                stopLoading(view)
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

    fun stopLoading(view: WebView) {
        progressBar.visibility = View.GONE
        view.stopLoading()
    }

    fun finishActivity(activity: Activity) {
        webViewActivity.setResult(Activity.RESULT_OK)
        webViewActivity.finish()
    }
}
