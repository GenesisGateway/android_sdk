package com.emerchantpay.gateway.genesisandroid.ui

import android.app.ProgressDialog
import android.os.Message
import android.webkit.WebView
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebChromeClient
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebView
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebViewActivity
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment



@RunWith(RobolectricTestRunner::class)
class GenesisWebChromeClientUnitTest {
    private var progressBar: ProgressDialog? = null
    private var genesisWebView: GenesisWebView? = null
    private var mActivity: GenesisWebViewActivity? = null
    private var genesisWebChromeClient: GenesisWebChromeClient? = null

    @Before
    fun setup() {
        progressBar = mock(ProgressDialog::class.java)
        genesisWebView = mock(GenesisWebView::class.java)
        mActivity = mock(GenesisWebViewActivity::class.java)
        genesisWebChromeClient = mock(GenesisWebChromeClient::class.java)
        `when`(mActivity!!.applicationContext).thenReturn(RuntimeEnvironment.application)

        // Invoke methods
        mActivity!!.pushNewWebView(genesisWebView!!)
        mActivity!!.popCurrentWebView()
        progressBar!!.dismiss()
    }

    @Test
    fun testOnCreateWindow() {
        val webViewTransport = mock(WebView.WebViewTransport::class.java)
        val message = mock(Message::class.java)

        webViewTransport.setWebView(genesisWebView)
        message.obj = webViewTransport
        message.sendToTarget()

        val result = genesisWebView?.let { genesisWebChromeClient?.onCreateWindow(it, false, false, message) }

        result?.let { assertFalse(it) }
        verify(webViewTransport).webView = any(GenesisWebView::class.java)
        verify(message).sendToTarget()
    }

    @Test
    fun testOnCloseWindow() {
        genesisWebView?.let { genesisWebChromeClient?.onCloseWindow(it) }

        verify(mActivity)?.popCurrentWebView()
    }

    @Test
    fun testOnProgressChanged1() {
        progressBar!!.progress = 42

        verify(progressBar)?.progress = 42
    }

    @Test
    fun testOnProgressChanged2() {
        progressBar!!.progress = 100

        verify(progressBar)?.dismiss()
    }
}
