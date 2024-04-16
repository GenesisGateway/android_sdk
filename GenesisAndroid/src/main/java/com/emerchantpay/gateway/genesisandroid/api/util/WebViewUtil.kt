package com.emerchantpay.gateway.genesisandroid.api.util

import android.app.Activity
import android.content.Intent
import com.emerchantpay.gateway.genesisandroid.api.constants.IntentExtras
import com.emerchantpay.gateway.genesisandroid.api.constants.URLConstants

class WebViewUtil {

    companion object {
        fun getResultIntent(url: String): Pair<Int?, Intent?> {
            val returnIntent = Intent()

            when {
                url.contains(URLConstants.FAILURE_ENDPOINT, ignoreCase = true) -> {
                    returnIntent.putExtra(IntentExtras.EXTRA_RESULT, "failure")
                    return Pair(Activity.RESULT_OK, returnIntent)
                }

                url.contains(URLConstants.CANCEL_ENDPOINT, ignoreCase = true) -> {
                    returnIntent.putExtra("cancel", "cancel")
                    return Pair(Activity.RESULT_CANCELED, returnIntent)
                }

                url.contains(URLConstants.SUCCESS_ENDPOINT, ignoreCase = true) -> {
                    returnIntent.putExtra("success", "success")
                    return Pair(Activity.RESULT_OK, returnIntent)
                }
            }
            return Pair(null, null)
        }
    }
}
