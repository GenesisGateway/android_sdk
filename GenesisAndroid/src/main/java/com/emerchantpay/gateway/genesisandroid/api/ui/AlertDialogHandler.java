package com.emerchantpay.gateway.genesisandroid.api.ui;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogHandler {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alert;

    public AlertDialogHandler(Context context, String title, String message) {
        super();

        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setNeutralButton("OK", null);

        alert = dialogBuilder.create();
    }

    public void show() {
        alert.show();
    }

    public void dismiss() {
        alert.dismiss();
    }
}