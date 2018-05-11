package com.emerchantpay.gateway.genesisandroid.api.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import com.emerchantpay.gateway.genesisandroid.api.constants.SharedPrefConstants;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;

public class GenesisSharedPreferences {

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getApplicationContext().getSharedPreferences("GenesisAPI", Context.MODE_PRIVATE);
    }

    public static void putParcelable(Context context, String key, Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        getSharedPreferences(context).edit()
                .putString(key, Base64.encodeToString(parcel.marshall(), 0))
                .apply();
    }

    public static Parcel getParcelable(Context context, String key) {
        try {
            byte[] requestBytes = Base64.decode(getSharedPreferences(context).getString(key, ""), 0);
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(requestBytes, 0, requestBytes.length);
            parcel.setDataPosition(0);

            return parcel;
        } catch (Exception ignored) {

        }

        return null;
    }

    public static void remove(Context context, String key) {
        getSharedPreferences(context)
                .edit()
                .remove(key)
                .apply();
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreferences(context)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static String getString(Context context, String key) {
        return getSharedPreferences(context)
                .getString(key, "");
    }

    public void loadSharedPreferences(Context context, PaymentRequest request) {
        if (context != null) {
            // Shared preferences
            this.putString(context, SharedPrefConstants.AMOUNT_KEY, String.valueOf(request.getAmount()));
            this.putString(context, SharedPrefConstants.CURRENCY_KEY, request.getCurrency());
            this.putString(context, SharedPrefConstants.USAGE_KEY, request.getUsage());
            this.putString(context, SharedPrefConstants.CUSTOMER_EMAIL_KEY, request.getCustomerEmail());
            this.putString(context, SharedPrefConstants.CUSTOMER_PHONE_KEY, request.getCustomerPhone());
            this.putString(context, SharedPrefConstants.NOTIFICATIONURL_KEY, request.getNotificationUrl());

            // Shared prefs for Billing address
            this.putString(context, SharedPrefConstants.FIRSTNAME_KEY, request.getPaymentAddress().getFirstName());
            this.putString(context, SharedPrefConstants.LASTNAME_KEY, request.getPaymentAddress().getLastname());
            this.putString(context, SharedPrefConstants.ADDRESS1_KEY, request.getPaymentAddress().getAddress1());
            this.putString(context, SharedPrefConstants.ADDRESS2_KEY, request.getPaymentAddress().getAddress2());
            this.putString(context, SharedPrefConstants.ZIPCODE_KEY, request.getPaymentAddress().getZipCode());
            this.putString(context, SharedPrefConstants.STATE_KEY, request.getPaymentAddress().getState());
            this.putString(context, SharedPrefConstants.CITY_KEY, request.getPaymentAddress().getCity());
            this.putString(context, SharedPrefConstants.COUNTRY_CODE_KEY, request.getPaymentAddress().getCountryCode());
            this.putString(context, SharedPrefConstants.COUNTRY_NAME_KEY, request.getPaymentAddress().getCountryName());
        }
    }
}