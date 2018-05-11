package com.emerchantpay.gateway.genesisandroid.api.internal;

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages;
import com.emerchantpay.gateway.genesisandroid.api.constants.TransactionTypes;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
import com.emerchantpay.gateway.genesisandroid.api.models.Country;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenesisValidator {

    // Regular expressions
    public static final Pattern VALID_EMAIL_REGEX = Pattern
            .compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PHONE_REGEX = Pattern.compile("^[0-9\\+]{1,}[0-9\\\\-]{3,15}$");
    public static final Pattern VALID_URL_REGEX = Pattern
            .compile("\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    private Boolean isValid;

    private ArrayList notValidParamsList = new ArrayList<String>();
    private ArrayList<String> emptyParamsList = new ArrayList<String>();

    // Genesis error handler
    private GenesisError error;

    public GenesisValidator() {
        super();
    }

    // Validate amount
    public Boolean validateAmount(BigDecimal amount) {
        if (amount.doubleValue() > 0 && amount != null) {
            isValid = true;
        } else {
            isValid = false;
            error = new GenesisError(ErrorMessages.INVALID_AMOUNT);
            notValidParamsList.add(amount.toString());
        }

        return isValid;
    }

    public Boolean validateEmail(String email) {
        Matcher m = VALID_EMAIL_REGEX.matcher(email);

        if (m.matches() && email != null && !email.isEmpty()) {
            isValid = true;
        } else {
            isValid = false;
            error = new GenesisError(ErrorMessages.INVALID_EMAIL);
            notValidParamsList.add(email);
        }

        return isValid;
    }

    public Boolean validatePhone(String phone) {
        Matcher m = VALID_PHONE_REGEX.matcher(phone);

        if (m.matches() && phone != null && !phone.isEmpty()) {
            isValid = true;
        } else {
            isValid = false;
            error = new GenesisError(ErrorMessages.INVALID_PHONE);
            notValidParamsList.add(phone);
        }

        return isValid;
    }

    // Validate Url
    public Boolean validateNotificationUrl(String url) {
        Matcher m = VALID_URL_REGEX.matcher(url);

        if (m.matches() && url != null && !url.isEmpty()) {
            isValid = true;
        } else {
            isValid = false;
            error = new GenesisError(ErrorMessages.INVALID_NOTIFICATION_URL);
            notValidParamsList.add(url);
        }

        return isValid;
    }

    public Boolean validateTransactionType(String transactionType) throws IllegalAccessException {
        Boolean isMatch = false;

        Field[] fields = TransactionTypes.class.getDeclaredFields();

        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers())
                    && transactionType.equals(f.get(f.getName()))) {
                isMatch = true;
            }
        }

        if (isMatch == true) {
            return true;
        } else {
            error = new GenesisError(ErrorMessages.INVALID_TRANSACTION_TYPE, transactionType);
            notValidParamsList.add(transactionType);
            return false;
        }
    }

    public Boolean validateString(String param, String value) {
        if (value == null || value.isEmpty() || value.equals("null")) {
            isValid = false;
            error = new GenesisError(ErrorMessages.EMPTY_PARAM, param);
            if (!emptyParamsList.contains(param)) {
                emptyParamsList.add(param);
            }
        } else {
            isValid = true;
        }

        return isValid;
    }

    public Boolean isValidRequest(PaymentRequest request) {
        validateAmount(request.getAmount());
        validateEmail(request.getCustomerEmail());
        validatePhone(request.getCustomerPhone());
        validateNotificationUrl(request.getNotificationUrl());

        // Validate empty string values
        validateString("transaction_id", request.getTransactionId());
        validateString("amount", String.valueOf(request.getAmount()));
        validateString("customer_email", request.getCustomerEmail());
        validateString("customer_phone", request.getCustomerPhone());
        validateString("notification_url", request.getNotificationUrl());

        if (notValidParamsList != null && !notValidParamsList.isEmpty() && !isValidData()) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean isValidAddress(PaymentAddress address) {
        validateString("firstname", address.getFirstName());
        validateString("lastname", address.getLastname());
        validateString("address1", address.getAddress1());
        validateString("zipcode", address.getZipCode());

        // State
        if (address.getCountryCode().equals(Country.UnitedStates.getCode())
                || address.getCountryCode().equals(Country.Canada.getCode())) {
            validateString("state", address.getState());
        }

        validateString("city", address.getCity());
        validateString("country", address.getCountryCode());

        if (!isValidData()) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean isValidData() {
        StringBuilder emptyParams = new StringBuilder();

        // Empty params
        for (Integer i = 0; i < emptyParamsList.size(); i++) {
            if (i == 0) {
                emptyParams.append(emptyParamsList.get(0));
            } else {
                emptyParams.append(", " + emptyParamsList.get(i));
            }
        }

        if (!emptyParamsList.isEmpty()) {
            if (emptyParamsList.size() > 1) {
                error = new GenesisError(ErrorMessages.EMPTY_PARAMS, String.valueOf(emptyParams));
            } else {
                error = new GenesisError(ErrorMessages.EMPTY_PARAM, String.valueOf(emptyParams));
            }
            return false;
        } else if (!notValidParamsList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public GenesisError getError() {
        return error;
    }
}
