package com.emerchantpay.gateway.genesisandroid.api.ui

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar

open class GenesisWebView : WebView {

    // Progress Bar
    private var progressBar: ProgressBar? = null

    @SuppressLint("SetJavaScriptEnabled")
    constructor(context: Context, progressBar: ProgressBar) : super(context) {
        this.progressBar = progressBar

        prepareProgressBar()
    }

    constructor(context: Context, attributes: AttributeSet, progressBar: ProgressBar) : super(context, attributes) {
        this.progressBar = progressBar

        prepareProgressBar()
    }

    constructor(context: Context, attributes: AttributeSet, defStyle: Int,
                progressBar: ProgressBar) : super(context, attributes, defStyle) {
        this.progressBar = progressBar

        prepareProgressBar()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attributes: AttributeSet, defStyleAttr: Int, defStyleRes: Int,
                progressBar: ProgressBar) : super(context, attributes, defStyleAttr, defStyleRes) {
        this.progressBar = progressBar

        prepareProgressBar()
    }

    fun init(activity: GenesisWebViewActivity) {
        id = android.R.id.widget_frame

        val settings = settings
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        settings.setSupportMultipleWindows(true)
        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.domStorageEnabled = true
        settings.databaseEnabled = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
        }

        webViewClient = progressBar?.let { GenesisWebViewClient(activity, it) }!!
        webChromeClient = progressBar?.let { GenesisWebChromeClient(activity, it) }
    }

    protected fun prepareProgressBar() {
        // Prepare for a Progress bar dialog
        progressBar!!.visibility = View.VISIBLE
        progressBar!!.bringToFront()
        progressBar!!.progress = 0
        progressBar!!.max = 100
    }
}
