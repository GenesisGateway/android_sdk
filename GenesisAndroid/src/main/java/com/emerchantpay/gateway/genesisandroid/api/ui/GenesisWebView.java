package com.emerchantpay.gateway.genesisandroid.api.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class GenesisWebView extends WebView {

    // Progress Bar
    private ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    public GenesisWebView(Context context, ProgressBar progressBar) {
        super(context);
        this.progressBar = progressBar;

        prepareProgressBar();
    }

    public GenesisWebView(Context context, AttributeSet attributes, ProgressBar progressBar) {
        super(context, attributes);
        this.progressBar = progressBar;

        prepareProgressBar();
    }

    public GenesisWebView(Context context, AttributeSet attributes, int defStyle,
                          ProgressBar progressBar) {
        super(context, attributes, defStyle);
        this.progressBar = progressBar;

        prepareProgressBar();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GenesisWebView(Context context, AttributeSet attributes, int defStyleAttr, int defStyleRes,
                          ProgressBar progressBar) {
        super(context, attributes, defStyleAttr, defStyleRes);
        this.progressBar = progressBar;

        prepareProgressBar();
    }

    protected void init(GenesisWebViewActivity activity) {
        setId(android.R.id.widget_frame);

        WebSettings settings = getSettings();
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }

        setWebViewClient(new GenesisWebViewClient(activity, progressBar));
        setWebChromeClient(new GenesisWebChromeClient(activity, progressBar));
    }

    protected void prepareProgressBar() {
        // Prepare for a Progress bar dialog
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        progressBar.setProgress(0);
        progressBar.setMax(100);
    }
}
