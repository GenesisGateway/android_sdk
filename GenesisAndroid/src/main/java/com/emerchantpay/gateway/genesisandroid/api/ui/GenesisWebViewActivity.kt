package com.emerchantpay.gateway.genesisandroid.api.ui

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.emerchantpay.gateway.genesisandroid.R
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras
import com.emerchantpay.gateway.genesisandroid.api.internal.Genesis
import com.emerchantpay.gateway.genesisandroid.api.internal.request.ReconcileRequest
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration
import java.util.*

open class GenesisWebViewActivity : Activity() {

    // UI Controls
    private var mActionBar: ActionBar? = null
    private var mRootView: FrameLayout? = null
    private var progressBar: ProgressBar? = null
    private var genesisWebViews: Stack<GenesisWebView>? = null

    var error: GenesisError? = null
        private set

    // Get View Context
    val context: Context
        get() = mRootView!!.context

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_PROGRESS)
        setContentView(R.layout.activity_genesis_web_view)
        setupActionBar()

        // UI Controls
        genesisWebViews = Stack()
        mRootView = findViewById<View>(R.id.frameLayout) as FrameLayout
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar

        // Get Redirect Url
        val redirectUrl = intent.getStringExtra(IntentExtras.EXTRA_REDIRECT_URL)

        // Load WebView
        val webView = GenesisWebView(this, progressBar!!)
        webView.init(this)
        webView.loadUrl(redirectUrl)
        pushNewWebView(webView)
    }

    fun pushNewWebView(webView: GenesisWebView) {
        genesisWebViews?.push(webView)
        mRootView?.removeAllViews()
        mRootView?.addView(webView)
    }

    fun popCurrentWebView() {
        genesisWebViews?.pop()
        genesisWebViews?.pop()?.let { pushNewWebView(it) }
    }

    fun finishWithResult(error: GenesisError?) {
        setResult(RESULT_OK, Intent()
                .putExtra(IntentExtras.EXTRA_RESULT,
                        error!!.technicalMessage))
        finish()
    }

    override fun onBackPressed() {
        when {
            genesisWebViews!!.peek().canGoBack() -> genesisWebViews!!.peek().goBack()
            genesisWebViews!!.size > 1 -> popCurrentWebView()
            else -> super.onBackPressed()
        }
    }

    fun setActionBarTitle(title: String) {
        when {
            mActionBar != null -> mActionBar!!.title = title
        }
    }

    private fun setupActionBar() {
        mActionBar = actionBar
        if (mActionBar != null) {
            setActionBarTitle("")
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    fun executeReconcile() {
        val uniqueId = this.intent.extras!!.getString(IntentExtras.EXTRA_UNIQUE_ID)
        val configuration = this.intent.extras!!
                .getSerializable(IntentExtras.EXTRA_CONFIGURATION) as Configuration

        val reconcileRequest = ReconcileRequest()
        uniqueId?.let { reconcileRequest.setUniqueId(it) }

        val genesis = Genesis(this, configuration, reconcileRequest)
        genesis.push()

        val response = genesis.response

        when {
            response?.isSuccess == false -> {
                error = response.error

                finishWithResult(error)
            }
        }
    }
}