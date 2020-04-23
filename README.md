# Genesis Android SDK

[![Build Status](https://img.shields.io/travis/GenesisGateway/android_sdk.svg?style=flat)](https://travis-ci.org/GenesisGateway/android_sdk)
[![Software License](https://img.shields.io/badge/license-MIT-green.svg?style=flat)](LICENSE)

## Table of Contents
- [Requirements](#requirements)
- [Installation and Setup](#installation-and-setup)
- [Basic Usage](#basic-usage)
- [Additional Usage](#additional-usage)


## Requirements

- JDK >= 1.8
- Gradle >= 4.1
- Android >=  4.4
- Android Studio >= 3.0

Installation and Setup
------------

* clone or [download](https://github.com/GenesisGateway/android_sdk/archive/master.zip) this repo

```sh
git clone http://github.com/GenesisGateway/android_sdk GenesisAndroid
cd GenesisAndroid
```

#### Gradle
* Add the dependency in your build.gradle:
```
dependencies {
  implementation 'com.emerchantpay.gateway:genesis-android:1.2.4'
}
```

Basic Usage
------------------

* AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="YOUR_PACKAGE">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <uses-sdk android:minSdkVersion="19" android:targetSdkVersion="27" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name="com.emerchantpay.gateway.genesisandroid.api.ui.GenesisWebViewActivity"/>
    </application>
</manifest>
```

* MainActivity.java

```kotlin
import android.R
import android.app.Activity
import android.os.Bundle
import android.view.View

import com.emerchantpay.gateway.androidgenesissample.R
import com.emerchantpay.gateway.genesisandroid.api.ui.AlertDialogHandler
import com.emerchantpay.gateway.genesisandroid.api.constants.Endpoints
import com.emerchantpay.gateway.genesisandroid.api.constants.Environments
import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages
import com.emerchantpay.gateway.genesisandroid.api.constants.Locales
import com.emerchantpay.gateway.genesisandroid.api.internal.Genesis
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.TransactionTypesRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.response.Response
import com.emerchantpay.gateway.genesisandroid.api.models.Country
import com.emerchantpay.gateway.genesisandroid.api.models.Currency
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress
import com.emerchantpay.gateway.genesisandroid.api.models.WPFTransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration

import java.math.BigDecimal
import java.util.UUID

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @Throws(IllegalAccessException::class)
    fun loadPaymentPage(view: View) {

        // Generate unique Id
        val uniqueId = UUID.randomUUID().toString()

        // Create configuration
        val configuration = Configuration("SET_YOUR_USERNAME",
                "SET_YOUR_PASSWORD",
                Environments.STAGING, Endpoints.EMERCHANTPAY, Locales.EN)

        // Enable Debug mode
        configuration.setDebugMode(true)

        // Alert dialog
        var dialogHandler: AlertDialogHandler

        // Create Billing PaymentAddress
        val billingAddress = PaymentAddress("John", "Doe",
                "Fifth avenue 1", "Fifth avenue 1", "10000", "New York",
                "Washington", Country.UnitedStates)

        // Create Transaction types
        val transactionTypes = TransactionTypesRequest()
        transactionTypes.addTransaction(WPFTransactionTypes.sale)

        // Init WPF API request
        val paymentRequest = PaymentRequest(this, uniqueId,
                BigDecimal("2.00"), Currency.USD,
                "john@example.com", "+555555555", billingAddress,
                "https://example.com", transactionTypes)

        val genesis = Genesis(this, configuration, paymentRequest)

        // Genesis Error handler
        var error: GenesisError?// Get Error Handler

        when {
            genesis.isConnected(this)!! -> {
                dialogHandler = AlertDialogHandler(this, "Error",
                        ErrorMessages.CONNECTION_ERROR)
                dialogHandler.show()
            }
        }

        when {
            genesis.isConnected(this)!! && genesis.isValidData!! -> {
                //Execute WPF API request
                genesis.push()

                // Get response
                val response = genesis.response

                // Check if response isSuccess
                when {
                    response!!.isSuccess!! -> {
                        // Get Error Handler
                        error = response!!.error

                        dialogHandler = AlertDialogHandler(this, "Failure",
                                "Code: " + error!!.code + "\nMessage: "
                                        + error!!.message)
                        dialogHandler.show()
                    }
                }
            }
        }

        when {
            genesis.isValidData!! -> {
                // Get Error Handler
                error = genesis.error

                val message = error!!.message
                val technicalMessage: String?

                when {
                    error!!.technicalMessage != null && !error!!.technicalMessage!!.isEmpty() -> technicalMessage = error!!.technicalMessage
                    else -> technicalMessage = ""
                }

                dialogHandler = AlertDialogHandler(this, "Invalid",
                        "$technicalMessage $message")

                dialogHandler.show()
            }
        }
    }
}
```

## Additional Usage

Set usage, description, lifetime

```kotlin
paymentRequest.setUsage("TICKETS")
        paymentRequest.setDescription("Description")
        paymentRequest.setLifetime(60)
```

Set shipping address

```kotlin
val shippingAddress = PaymentAddress("John", "Doe",
                "Fifth avenue 1", "Fifth avenue 1", "10000", "New York",
                "Washington", Country.UnitedStates)

paymentRequest.setShippingAddress(shippingAddress)
```

Set Risk Params

```kotlin
// Risk params
val riskParams = RiskParams("1002547", "1DA53551-5C60-498C-9C18-8456BDBA74A9",
                "987-65-4320", "12-34-56-78-9A-BC", "123456",
                "emil@example.com", "+49301234567", "245.253.2.12",
                "10000000000", "1234", "100000000", "John",
                "Doe", "US", "test", "245.25 3.2.12",
                "test", "test123456", "Bin name",
                "+49301234567")

paymentRequest.setRiskParams(riskParams)
```

Running Tests
--------------

* ./gradlew test