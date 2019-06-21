package com.emerchantpay.gateway.genesisandroid.api.internal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages;
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.ReconcileRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.response.Response;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebViewActivity;
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;

import java.net.URL;

public class Genesis {

    // Application context
    private Context context;

    // Configuration
    private Configuration configuration;

    // WPF request
    private PaymentRequest paymentRequest;

    // WPF Reconcile request
    private ReconcileRequest wpfReconcile;

    // Genesis client
    private GenesisClient client;

    // Genesis error handler
    private GenesisError error;

    public Genesis(Context context, Configuration configuration, PaymentRequest paymentRequest) {
        super();

        this.context = context;
        this.configuration = configuration;
        this.paymentRequest = paymentRequest;
    }

    public Genesis(Context context, Configuration configuration, ReconcileRequest wpfReconcile) {
        super();

        this.context = context;
        this.configuration = configuration;
        this.wpfReconcile = wpfReconcile;
    }

    public void push() {
        // Create GatewayClient
        if (paymentRequest != null) {
            client = new GenesisClient(configuration, paymentRequest);
        } else if (wpfReconcile != null) {
            client = new GenesisClient(configuration, wpfReconcile);
        }

        // Set Debug mode
        client.debugMode(configuration.isDebugModeEnabled());

        // Execute Payment Request
        client.execute();

        // Get response
        Response response = getResponse();

        String redirectUrl = response.getRedirectUrl();
        String uniqueId = response.getUniqueId();

        if (redirectUrl != null) {
            // Start activity
            Intent intent = new Intent(context, GenesisWebViewActivity.class);
            intent.putExtra(IntentExtras.EXTRA_REDIRECT_URL, redirectUrl);
            intent.putExtra(IntentExtras.EXTRA_UNIQUE_ID, uniqueId);
            intent.putExtra(IntentExtras.EXTRA_CONFIGURATION, configuration);
            ((Activity) context).startActivityForResult(intent, 1);
        }
    }

    public void loadRedirectUrl(URL url) {
        if (url != null) {
            // Start activity
            Intent intent = new Intent(context, GenesisWebViewActivity.class);
            intent.putExtra(IntentExtras.EXTRA_REDIRECT_URL, String.valueOf(url));
            intent.putExtra(IntentExtras.EXTRA_CONFIGURATION, configuration);
            ((Activity) context).startActivityForResult(intent, 1);
        }
    }

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public ReconcileRequest getWpfReconcileRequest() {
        return wpfReconcile;
    }

    public Response getResponse() {
        return new Response(client);
    }

    // Check Network connection status
    public Boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        Boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected == false) {
            error = new GenesisError(ErrorMessages.CONNECTION_ERROR);
        }

        return isConnected;
    }

    public Boolean isValidData() {
        if (paymentRequest != null && paymentRequest.isValidData()) {
            return true;
        } else {
            return false;
        }
    }

    public GenesisError getError() {
        if (isValidData()) {
            error = getResponse().getError();
        } else {
            error = paymentRequest.getValidator().getError();
        }

        return error;
    }
}
