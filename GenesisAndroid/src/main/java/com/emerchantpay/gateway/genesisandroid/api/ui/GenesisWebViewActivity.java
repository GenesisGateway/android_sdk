package com.emerchantpay.gateway.genesisandroid.api.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.emerchantpay.gateway.genesisandroid.R;
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras;
import com.emerchantpay.gateway.genesisandroid.api.internal.Genesis;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.ReconcileRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.response.Response;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;

import java.util.Stack;

public class GenesisWebViewActivity extends Activity {

    // UI Controls
    private ActionBar mActionBar;
    private FrameLayout mRootView;
    private ProgressBar progressBar;
    private Stack<GenesisWebView> genesisWebViews;

    private GenesisError error;

    public GenesisWebViewActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_genesis_web_view);
        setupActionBar();

        // UI Controls
        genesisWebViews = new Stack<GenesisWebView>();
        mRootView = ((FrameLayout) findViewById(R.id.frameLayout));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Get Redirect Url
        String redirectUrl = getIntent().getStringExtra(IntentExtras.EXTRA_REDIRECT_URL);

        // Load WebView
        GenesisWebView webView = new GenesisWebView(this, progressBar);
        webView.init(this);
        webView.loadUrl(redirectUrl);
        pushNewWebView(webView);
    }

    public void pushNewWebView(GenesisWebView webView) {
        genesisWebViews.push(webView);
        mRootView.removeAllViews();
        mRootView.addView(webView);
    }

    public void popCurrentWebView() {
        genesisWebViews.pop();
        pushNewWebView(genesisWebViews.pop());
    }

    public void finishWithResult(GenesisError error) {
        setResult(Activity.RESULT_OK, new Intent()
                .putExtra(IntentExtras.EXTRA_RESULT,
                        error.getTechnicalMessage()));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (genesisWebViews.peek().canGoBack()) {
            genesisWebViews.peek().goBack();
        } else if (genesisWebViews.size() > 1) {
            popCurrentWebView();
        } else {
            super.onBackPressed();
        }
    }

    public void setActionBarTitle(String title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

    private void setupActionBar() {
        mActionBar = getActionBar();
        if (mActionBar != null) {
            setActionBarTitle("");
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Get View Context
    public Context getContext() {
        return mRootView.getContext();
    }

    public void executeReconcile() {
        String uniqueId = this.getIntent().getExtras().getString(IntentExtras.EXTRA_UNIQUE_ID);
        Configuration configuration = (Configuration) this.getIntent().getExtras()
                .getSerializable(IntentExtras.EXTRA_CONFIGURATION);

        ReconcileRequest reconcileRequest = new ReconcileRequest();
        reconcileRequest.setUniqueId(uniqueId);

        Genesis genesis = new Genesis(this, configuration, reconcileRequest);
        genesis.push();

        Response response = genesis.getResponse();

        if (!response.isSuccess()) {
            error = response.getError();

            finishWithResult(error);
        }
    }

    public GenesisError getError() {
        return error;
    }
}