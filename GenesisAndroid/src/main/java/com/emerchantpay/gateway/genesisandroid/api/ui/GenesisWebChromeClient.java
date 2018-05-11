package com.emerchantpay.gateway.genesisandroid.api.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.emerchantpay.gateway.genesisandroid.api.constants.URLConstants;

public class GenesisWebChromeClient extends WebChromeClient {

    // WebView Activity
    private GenesisWebViewActivity webViewActivity;

    // Progress Bar
    private ProgressBar progressBar;

    public GenesisWebChromeClient(GenesisWebViewActivity activity, ProgressBar progressBar) {
        this.webViewActivity = activity;
        this.progressBar = progressBar;
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        GenesisWebView newWebView = new GenesisWebView(webViewActivity.getApplicationContext(), progressBar);
        newWebView.init(webViewActivity);
        webViewActivity.pushNewWebView(newWebView);
        ((WebView.WebViewTransport) resultMsg.obj).setWebView(newWebView);
        resultMsg.sendToTarget();

        // Show Progress bar
        progressBar.setVisibility(View.VISIBLE);

        String url = view.getUrl();

        Intent returnIntent = new Intent();

        if (url.equals(URLConstants.FAILURE_URL.toLowerCase())) {
            webViewActivity.executeReconcile();

            stopLoading(view);
        } else if (url.equals(URLConstants.CANCEL_URL.toLowerCase())) {
            stopLoading(view);
            
            returnIntent.putExtra("cancel", "cancel");
            finishActivity(webViewActivity);
        } else if (url.equals(URLConstants.SUCCESS_URL.toLowerCase())) {
            returnIntent.putExtra("success", "success");
            finishActivity(webViewActivity);

            stopLoading(view);
        }

        return true;
    }

    @Override
    public void onCloseWindow(WebView window) {
        webViewActivity.popCurrentWebView();

        // Dismiss Progress bar
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress < 100) {
            webViewActivity.setProgress(newProgress);
            webViewActivity.setProgressBarVisibility(true);
        } else {
            webViewActivity.setProgressBarVisibility(false);

            // Dismiss Progress bar
            progressBar.setVisibility(View.GONE);
        }
    }

    public void stopLoading(WebView view) {
        progressBar.setVisibility(View.GONE);
        view.stopLoading();
    }

    public void finishActivity(Activity activity) {
        webViewActivity.setResult(Activity.RESULT_OK);
        webViewActivity.finish();
    }
}
