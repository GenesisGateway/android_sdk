package com.emerchantpay.gateway.genesisandroid.ui;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;

import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebView;
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebViewActivity;
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebViewClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class GenesisWebViewClientUnitTest {
    private GenesisWebViewActivity mActivity;
    private GenesisWebView mWebView;
    private GenesisWebViewClient mWebViewClient;

    @Before
    public void setup() {
        mActivity = mock(GenesisWebViewActivity.class);
        mWebView = mock(GenesisWebView.class);
        mWebViewClient = mock(GenesisWebViewClient.class);

        // Invoke methods
        mActivity.setActionBarTitle("TEST");
        mWebView.stopLoading();
        mActivity.finishWithResult(new GenesisError(100, "Error"));
    }

    @Test
    public void testOnPageStarted() {
        mWebViewClient.onPageStarted(mWebView, "https:google.com", null);

        verify(mWebView, times(1)).stopLoading();
        verify(mActivity, times(1)).finishWithResult(any(GenesisError.class));
    }

    @Test
    public void testOnPageFinished() {
        mWebViewClient.onPageFinished(mWebView, null);

        verify(mActivity, times(1)).setActionBarTitle("TEST");
    }

    @Test
    public void testReceivedError() {
        mWebViewClient.onReceivedError(mWebView, 0, "TEST", "");

        verify(mWebView, times(1)).stopLoading();
        verify(mActivity, times(1)).finishWithResult(any(GenesisError.class));
    }

    @Test
    public void testReceivedSslError() {
        SslErrorHandler handler = mock(SslErrorHandler.class);
        mWebViewClient.onReceivedSslError(mWebView, handler, mock(SslError.class));
        handler.cancel();

        verify(mWebView, times(1)).stopLoading();
        verify(handler, times(1)).cancel();
        verify(mActivity, times(1)).finishWithResult(any(GenesisError.class));
    }
}
