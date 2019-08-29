package com.emerchantpay.gateway.genesisandroid.api.ui

import android.app.AlertDialog
import android.content.Context


open class AlertDialogHandler(context: Context, title: String, message: String) {

    private var dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
    private var alert: AlertDialog? = null

    init {
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setNeutralButton("OK", null)

        alert = dialogBuilder.create()
    }

    fun show() {
        alert?.show()
    }

    fun dismiss() {
        alert?.dismiss()
    }
}