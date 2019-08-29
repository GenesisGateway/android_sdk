package com.emerchantpay.gateway.genesisandroid.ui

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebView
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebViewActivity
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebViewClient
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class GenesisWebViewClientUnitTest {
    private var mActivity: GenesisWebViewActivity? = null
    private var mWebView: GenesisWebView? = null
    private var mWebViewClient: GenesisWebViewClient? = null

    @Before
    fun setup() {
        mActivity = mock(GenesisWebViewActivity::class.java)
        mWebView = mock(GenesisWebView::class.java)
        mWebViewClient = mock(GenesisWebViewClient::class.java)

        // Invoke methods
        mActivity?.setActionBarTitle("TEST")
        mWebView?.stopLoading()
        mActivity?.finishWithResult(GenesisError(100, "Error"))
    }

    @Test
    fun testOnPageStarted() {
        val w = 100
        val h = 100

        val conf = Bitmap.Config.ARGB_8888
        val bmp = Bitmap.createBitmap(w, h, conf) // creating a MUTABLE bitmap

        mWebView?.let { mWebViewClient?.onPageStarted(it, "https:google.com", bmp) }

        verify(mWebView, times(1))?.stopLoading()
        verify(mActivity, times(1))?.finishWithResult(any(GenesisError::class.java))
    }

    @Test
    fun testOnPageFinished() {
        mWebView?.let { mWebViewClient?.onPageFinished(it, "") }

        verify(mActivity, times(1))?.setActionBarTitle("TEST")
    }

    @Test
    fun testReceivedError() {
        mWebViewClient?.onReceivedError(mWebView!!, 0, "TEST", "")

        verify(mWebView, times(1))?.stopLoading()
        verify(mActivity, times(1))?.finishWithResult(any(GenesisError::class.java))
    }

    @Test
    fun testReceivedSslError() {
        val handler = mock(SslErrorHandler::class.java)
        mWebViewClient?.onReceivedSslError(mWebView!!, handler, mock(SslError::class.java))
        handler.cancel()

        verify(mWebView, times(1))?.stopLoading()
        verify(handler, times(1))?.cancel()
        verify(mActivity, times(1))?.finishWithResult(any(GenesisError::class.java))
    }
}
