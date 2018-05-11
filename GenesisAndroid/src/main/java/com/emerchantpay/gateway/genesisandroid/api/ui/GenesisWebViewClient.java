package com.emerchantpay.gateway.genesisandroid.api.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages;
import com.emerchantpay.gateway.genesisandroid.api.constants.URLConstants;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;

public class GenesisWebViewClient extends WebViewClient {

    // WebView Activity
    private GenesisWebViewActivity webViewActivity;

    // Progress Bar
    private ProgressBar progressBar;

    public GenesisWebViewClient(GenesisWebViewActivity activity, ProgressBar progressBar) {
        this.webViewActivity = activity;
        this.progressBar = progressBar;
    }

    public void onPageStarted(WebView view, String url, Bitmap icon) {
        super.onPageStarted(view, url, icon);

        // Show Progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Destroy webview activity
        Intent returnIntent = new Intent();

        if (url.equals(URLConstants.FAILURE_URL.toLowerCase())) {
            webViewActivity.executeReconcile();

            stopLoading();
        } else if (url.equals(URLConstants.CANCEL_URL.toLowerCase())) {
            returnIntent.putExtra("cancel", "cancel");
            webViewActivity.setResult(Activity.RESULT_CANCELED, returnIntent);

            stopLoading();
        } else if (url.equals(URLConstants.SUCCESS_URL.toLowerCase())) {
            returnIntent.putExtra("success", "success");
            webViewActivity.setResult(Activity.RESULT_OK, returnIntent);

            stopLoading();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        webViewActivity.setActionBarTitle(view.getTitle());

        // Dismiss Progress bar
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description,
                                String failingUrl) {
        finishActivity(view, errorCode, description);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.cancel();
        finishActivity(view, error.getPrimaryError(), ErrorMessages.SSL_ERROR);
    }

    public void stopLoading() {
        progressBar.setVisibility(View.GONE);
        webViewActivity.finish();
    }

    public void finishActivity(WebView view, int errorCode, String description) {
        view.stopLoading();
        webViewActivity.finishWithResult(new GenesisError(errorCode, description));
    }
}
