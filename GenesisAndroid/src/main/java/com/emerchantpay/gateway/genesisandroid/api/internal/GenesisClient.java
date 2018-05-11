package com.emerchantpay.gateway.genesisandroid.api.internal;

import android.util.Log;

import com.emerchantpay.gateway.genesisandroid.api.network.HttpAsyncTask;
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper;
import com.emerchantpay.gateway.genesisandroid.api.util.Request;

import java.util.concurrent.ExecutionException;

public class GenesisClient extends Request {

    private Configuration configuration;
    private Request request;

    // Execute
    private HttpAsyncTask http;
    private NodeWrapper response;

    public GenesisClient(Configuration configuration, Request request) {

        super();
        this.configuration = configuration;
        this.request = request;
    }

    public GenesisClient debugMode(Boolean enabled) {
        configuration.setDebugMode(enabled);
        return this;
    }

    public GenesisClient changeRequest(Request request) {
        this.request = request;
        return this;
    }

    public TransactionGateway getTransaction() {

        return new TransactionGateway(configuration, getResponse());
    }

    public Request execute() {
        switch (request.getTransactionType()) {
            case "wpf_payment":
                configuration.setWpfEnabled(true);
                configuration.setTokenEnabled(false);

                if (configuration.getLanguage() != null) {
                    configuration.setAction(configuration.getLanguage() + "/wpf");
                } else {
                    configuration.setAction("wpf");
                }
                break;
            case "wpf_reconcile":
                configuration.setWpfEnabled(true);
                configuration.setTokenEnabled(false);
                configuration.setAction("wpf/reconcile");
                break;
            case "reconcile":
                configuration.setWpfEnabled(false);
                configuration.setTokenEnabled(true);
                configuration.setAction("reconcile");
                break;
            case "reconcile_by_date":
                configuration.setWpfEnabled(false);
                configuration.setTokenEnabled(true);
                configuration.setAction("reconcile/by_date");
                break;
            case "blacklist":
                configuration.setWpfEnabled(false);
                configuration.setTokenEnabled(false);
                configuration.setAction("blacklists");
                break;
            case "chargeback":
                configuration.setWpfEnabled(false);
                configuration.setTokenEnabled(false);
                configuration.setAction("chargebacks");
                break;
            case "chargeback_by_date":
                configuration.setWpfEnabled(false);
                configuration.setTokenEnabled(false);
                configuration.setAction("chargebacks/by_date");
                break;
            case "reports_fraud":
                configuration.setWpfEnabled(false);
                configuration.setTokenEnabled(false);
                configuration.setAction("fraud_reports");
                break;
            case "reports_fraud_by_date":
                configuration.setWpfEnabled(false);
                configuration.setTokenEnabled(false);
                configuration.setAction("fraud_reports/by_date");
                break;
            case "retrieval_requests":
                configuration.setWpfEnabled(false);
                configuration.setTokenEnabled(false);
                configuration.setAction("retrieval_requests");
                break;
            case "retrieval_requests_by_date":
                configuration.setWpfEnabled(false);
                configuration.setTokenEnabled(false);
                configuration.setAction("retrieval_requests/by_date");
                break;
            default:
                configuration.setWpfEnabled(false);
                configuration.setTokenEnabled(true);
                configuration.setAction("process");
                break;
        }

        http = new HttpAsyncTask(configuration);

        try {
            response = http.execute(configuration.getBaseUrl(), request).get();
        } catch (InterruptedException e) {
            Log.e("Interrupted Exception", e.toString());
        } catch (ExecutionException e) {
            Log.e("Execution Exception", e.toString());
        }

        return this;
    }

    public NodeWrapper getResponse() {
        return response;
    }
}
