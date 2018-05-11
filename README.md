# Genesis Android SDK

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
  implementation project(':GenesisAndroid:sdk')
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
        <activity android:name="com.emerchantpay.android.api.ui.GenesisWebViewActivity"/>
    </application>
</manifest>
```

* MainActivity.java

```java
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.emerchantpay.gateway.androidgenesissample.R;
import com.emerchantpay.gateway.androidgenesissample.handlers.AlertDialogHandler;
import com.emerchantpay.gateway.genesisandroid.api.constants.Endpoints;
import com.emerchantpay.gateway.genesisandroid.api.constants.Environments;
import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages;
import com.emerchantpay.gateway.genesisandroid.api.constants.Locales;
import com.emerchantpay.gateway.genesisandroid.api.internal.Genesis;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.response.Response;
import com.emerchantpay.gateway.genesisandroid.api.models.Country;
import com.emerchantpay.gateway.genesisandroid.api.models.Currency;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;
import com.emerchantpay.gateway.genesisandroid.api.models.WPFTransactionTypes;
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadPaymentPage(View view) throws IllegalAccessException {

        // Generate unique Id
        String uniqueId = UUID.randomUUID().toString();

        // Create configuration
        Configuration configuration = new Configuration("SET_YOUR_USERNAME",
                "SET_YOUR_PASSWORD",
                Environments.STAGING, Endpoints.EMERCHANTPAY, Locales.EN);

        // Enable Debug mode
        configuration.setDebugMode(true);

        // Alert dialog
        AlertDialogHandler dialogHandler;

        // Create Billing PaymentAddress
        PaymentAddress billingAddress = new PaymentAddress("John", "Doe",
                "Fifth avenue 1", "Fifth avenue 1", "10000", "New York",
                "Washington", new Country().UnitedStates);

        // Create Transaction types
        ArrayList<String> transactionTypes = new ArrayList<String>();
        transactionTypes.add(WPFTransactionTypes.sale);

        // Init WPF API request
        PaymentRequest paymentRequest = new PaymentRequest(this, uniqueId,
                new BigDecimal("2.00"), new Currency().USD,
                "john@example.com", "+555555555", billingAddress,
                "https://example.com", transactionTypes);

        Genesis genesis = new Genesis(this, configuration, paymentRequest);

        // Genesis Error handler
        GenesisError error;

        if (!genesis.isConnected(this)) {
            dialogHandler = new AlertDialogHandler(this, "Error",
                    ErrorMessages.CONNECTION_ERROR);
            dialogHandler.show();
        }

        if (genesis.isConnected(this) && genesis.isValidData()) {
            //Execute WPF API request
            genesis.push();

            // Get response
            Response response = genesis.getResponse();

            // Check if response isSuccess
            if (!response.isSuccess()) {
                // Get Error Handler
                error = response.getError();

                dialogHandler = new AlertDialogHandler(this, "Failure",
                        "Code: " + error.getCode() + "\nMessage: "
                                + error.getMessage());
                dialogHandler.show();
            }
        }

        if (!genesis.isValidData()) {
            // Get Error Handler
            error = genesis.getError();

            String message = error.getMessage();
            String technicalMessage;

            if (error.getTechnicalMessage() != null && !error.getTechnicalMessage().isEmpty()) {
                technicalMessage = error.getTechnicalMessage();
            } else {
                technicalMessage = "";
            }

            dialogHandler = new AlertDialogHandler(this, "Invalid",
                    technicalMessage + " " + message);

            dialogHandler.show();
        }
    }
}
```

## Additional Usage

Set usage, description, lifetime

```java
paymentRequest.setUsage("TICKETS");
paymentRequest.setDescription("Description");
paymentRequest.setLifetime(60);
```

Set shipping address

```java
PaymentAddress shippingAddress = nnew PaymentAddress("John", "Doe",
                "Fifth avenue 1", "Fifth avenue 1", "10000", "New York",
                "Washington", new Country().UnitedStates);

paymentRequest.setShippingAddress(shippingAddress);
```

Set Risk Params

```java
// Risk params
RiskParams riskParams = new RiskParams("1002547", "1DA53551-5C60-498C-9C18-8456BDBA74A9",
        "987-65-4320", "12-34-56-78-9A-BC", "123456",
        "emil@example.com", "+49301234567", "245.253.2.12",
        "10000000000", "1234", "100000000", "John",
        "Doe", "US", "test", "245.25 3.2.12",
        "test", "test123456", "Bin name",
        "+49301234567");

paymentRequest.setRiskParams(riskParams);
```

Running Tests
--------------

* ./gradlew test