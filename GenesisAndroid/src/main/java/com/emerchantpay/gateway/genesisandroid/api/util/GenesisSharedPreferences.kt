package com.emerchantpay.gateway.genesisandroid.api.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64

import com.emerchantpay.gateway.genesisandroid.api.constants.SharedPrefConstants
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest

class GenesisSharedPreferences {

    fun loadSharedPreferences(context: Context?, request: PaymentRequest) {
        if (context != null) {
            // Shared preferences
            this.putString(context, SharedPrefConstants.AMOUNT_KEY, request.amount.toString())
            request?.currency?.let { this.putString(context, SharedPrefConstants.CURRENCY_KEY, it) }
            this.putString(context, SharedPrefConstants.USAGE_KEY, request.usage)
            this.putString(context, SharedPrefConstants.CUSTOMER_EMAIL_KEY, request.customerEmail)
            this.putString(context, SharedPrefConstants.CUSTOMER_PHONE_KEY, request.customerPhone)
            this.putString(context, SharedPrefConstants.NOTIFICATIONURL_KEY, request.notificationUrl)

            // Shared prefs for Billing address
            request.paymentAddress?.firstName?.let { this.putString(context, SharedPrefConstants.FIRSTNAME_KEY, it) }
            request.paymentAddress?.lastname?.let { this.putString(context, SharedPrefConstants.LASTNAME_KEY, it) }
            request.paymentAddress?.address1?.let { this.putString(context, SharedPrefConstants.ADDRESS1_KEY, it) }
            request.paymentAddress?.address2?.let { this.putString(context, SharedPrefConstants.ADDRESS2_KEY, it) }
            request.paymentAddress?.zipCode?.let { this.putString(context, SharedPrefConstants.ZIPCODE_KEY, it) }
            request.paymentAddress?.state?.let { this.putString(context, SharedPrefConstants.STATE_KEY, it) }
            request.paymentAddress?.city?.let { this.putString(context, SharedPrefConstants.CITY_KEY, it) }
            request.paymentAddress?.countryCode?.let { this.putString(context, SharedPrefConstants.COUNTRY_CODE_KEY, it) }
            request.paymentAddress?.countryName?.let { this.putString(context, SharedPrefConstants.COUNTRY_NAME_KEY, it) }
        }
    }

    fun getSharedPreferences(context: Context?): SharedPreferences {
        return context?.applicationContext!!.getSharedPreferences("GenesisAPI", Context.MODE_PRIVATE)
    }

    fun putParcelable(context: Context, key: String, parcelable: Parcelable) {
        val parcel = Parcel.obtain()
        parcelable.writeToParcel(parcel, 0)
        getSharedPreferences(context).edit()
                .putString(key, Base64.encodeToString(parcel.marshall(), 0))
                .apply()
    }

    fun getParcelable(context: Context, key: String): Parcel? {
        try {
            val requestBytes = Base64.decode(getSharedPreferences(context).getString(key, ""), 0)
            val parcel = Parcel.obtain()
            parcel.unmarshall(requestBytes, 0, requestBytes.size)
            parcel.setDataPosition(0)

            return parcel
        } catch (ignored: Exception) {

        }

        return null
    }

    fun remove(context: Context, key: String) {
        getSharedPreferences(context)
                .edit()
                .remove(key)
                .apply()
    }

    fun putString(context: Context?, key: String, value: String) {
        getSharedPreferences(context)
                .edit()
                .putString(key, value)
                .apply()
    }

    fun getString(context: Context?, key: String): String? {
        return getSharedPreferences(context)
                .getString(key, "")
    }

    fun putInt(context: Context?, key: String, value: Int?) {
        getSharedPreferences(context)
                .edit()
                .putInt(key, value!!)
                .apply()
    }

    fun getInt(context: Context?, key: String): Int {
        return getSharedPreferences(context)
                .getInt(key, 0)
    }
}