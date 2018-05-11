package com.emerchantpay.gateway.genesisandroid.ui;

import android.app.ProgressDialog;
import android.os.Message;
import android.webkit.WebView;

import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebChromeClient;
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebView;
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebViewActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class GenesisWebChromeClientUnitTest {
    private ProgressDialog progressBar;
    private GenesisWebView genesisWebView;
    private GenesisWebViewActivity mActivity;
    private GenesisWebChromeClient genesisWebChromeClient;

    @Before
    public void setup() {
        progressBar = mock(ProgressDialog.class);
        genesisWebView = mock(GenesisWebView.class);
        mActivity = mock(GenesisWebViewActivity.class);
        genesisWebChromeClient = mock(GenesisWebChromeClient.class);
        when(mActivity.getApplicationContext()).thenReturn(RuntimeEnvironment.application);

        // Invoke methods
        mActivity.pushNewWebView(genesisWebView);
        mActivity.popCurrentWebView();
        progressBar.dismiss();
    }

    @Test
    public void testOnCreateWindow() {
        WebView.WebViewTransport webViewTransport = mock(GenesisWebView.WebViewTransport.class);
        Message message = mock(Message.class);

        webViewTransport.setWebView(genesisWebView);
        message.obj = webViewTransport;
        message.sendToTarget();

        boolean result = genesisWebChromeClient.onCreateWindow(genesisWebView, false, false, message);

        assertFalse(result);
        verify(mActivity).pushNewWebView(any(GenesisWebView.class));
        verify(webViewTransport).setWebView(any(GenesisWebView.class));
        verify(message).sendToTarget();
    }

    @Test
    public void testOnCloseWindow() {
        genesisWebChromeClient.onCloseWindow(null);

        verify(mActivity).popCurrentWebView();
    }

    @Test
    public void testOnProgressChanged1() {
        progressBar.setProgress(42);

        verify(progressBar).setProgress(42);
    }

    @Test
    public void testOnProgressChanged2() {
        progressBar.setProgress(100);

        verify(progressBar).dismiss();
    }
}
