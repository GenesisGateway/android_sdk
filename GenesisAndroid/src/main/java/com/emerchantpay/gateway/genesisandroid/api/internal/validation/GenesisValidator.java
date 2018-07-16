package com.emerchantpay.gateway.genesisandroid.api.internal.validation;

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages;
import com.emerchantpay.gateway.genesisandroid.api.constants.TransactionTypes;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.KlarnaItemsRequest;
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest;
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

    private ArrayList<String> notValidParamsList = new ArrayList<String>();
    private ArrayList<String> emptyParamsList = new ArrayList<String>();

    // Genesis error handler
    private GenesisError error;

    // Required params validator
    private RequiredParameters requiredParameters = new RequiredParameters();
    private RequiredParametersValidator requiredParametersValidator;

    public GenesisValidator() {
        super();
    }

    // Validate amount
    public Boolean validateAmount(BigDecimal amount) {
        if (amount.doubleValue() > 0 && amount != null) {
            return true;
        } else {
            error = new GenesisError(ErrorMessages.INVALID_AMOUNT);
            notValidParamsList.add(amount.toString());

            return false;
        }
    }

    public Boolean validateEmail(String email) {
        Matcher m = VALID_EMAIL_REGEX.matcher(email);

        if (m.matches() && email != null && !email.isEmpty()) {
            return true;
        } else {
            error = new GenesisError(ErrorMessages.INVALID_EMAIL);
            notValidParamsList.add(email);

            return false;
        }
    }

    public Boolean validatePhone(String phone) {
        Matcher m = VALID_PHONE_REGEX.matcher(phone);

        if (m.matches() && phone != null && !phone.isEmpty()) {
            return true;
        } else {
            error = new GenesisError(ErrorMessages.INVALID_PHONE);
            notValidParamsList.add(phone);

            return false;
        }
    }

    // Validate Url
    public Boolean validateNotificationUrl(String url) {
        Matcher m = null;

        if (url != null) {
            m = VALID_URL_REGEX.matcher(url);
        }

        if (m != null && m.matches() && url != null && !url.isEmpty()) {
            return true;
        } else {
            error = new GenesisError(ErrorMessages.INVALID_NOTIFICATION_URL);
            notValidParamsList.add(url);

            return false;
        }
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

            error = new GenesisError(ErrorMessages.EMPTY_PARAM, param);
            if (!emptyParamsList.contains(param)) {
                emptyParamsList.add(param);
            }

            return false;
        } else {
            return true;
        }
    }

    public Boolean isValidRequest(PaymentRequest request) {

        requiredParametersValidator = new RequiredParametersValidator(requiredParameters
                .getRequiredParametersForRequest(request));

        if (!requiredParametersValidator.isValidRequiredParams() && notValidParamsList != null
                && !notValidParamsList.isEmpty()) {
            error = requiredParametersValidator.getError();
            return false;
        } else {
            requiredParametersValidator = new RequiredParametersValidator();

            for (String transactionType : request.getTransactionTypes().getTransactionTypesList()) {
                requiredParametersValidator.setParameters(requiredParameters.getRequiredParametersForTransactionType(request, transactionType));
                if (!requiredParametersValidator.isValidRequiredParams()) {
                    String errorMessage = requiredParametersValidator.getError().getMessage();
                    String errorTechnicalMessage = requiredParametersValidator.getError().getTechnicalMessage();
                    error = new GenesisError("is required for " + transactionType + " transaction type",
                            errorTechnicalMessage + " parameter ");
                }
            }

            if (!requiredParametersValidator.isValidRequiredParams()) {
                return false;
            } else if (isValidRegex(request)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public Boolean isValidKlarnaRequest(KlarnaItemsRequest klarnaRequest, BigDecimal transactionAmount,
                                        BigDecimal orderTaxAmount) {
        if (klarnaRequest != null) {
            requiredParametersValidator = new RequiredParametersValidator(requiredParameters
                    .getRequiredParametersForKlarnaItem(klarnaRequest));
        } else {
            error = new GenesisError(ErrorMessages.REQUIRED_PARAMS, "items");
            return false;
        }

        if (klarnaRequest != null && requiredParametersValidator.isValidRequiredParams()
                && validateTransactionAmount(klarnaRequest, transactionAmount)
                && validateOrderTaxAmount(klarnaRequest, orderTaxAmount)) {
            return true;
        } else {
            return false;
        }
    }

    protected Boolean validateTransactionAmount(KlarnaItemsRequest klarnaRequest, BigDecimal transactionAmount) {
        // Transaction amount
        if (transactionAmount != null
                && transactionAmount.doubleValue() > 0
                && klarnaRequest.getTotalAmounts().doubleValue() > 0
                && transactionAmount.doubleValue() == klarnaRequest.getTotalAmounts().doubleValue()) {
            return true;
        } else {
            error = new GenesisError(ErrorMessages.INVALID_TOTAL_AMOUNTS);
            return false;
        }
    }

    protected Boolean validateOrderTaxAmount(KlarnaItemsRequest klarnaRequest, BigDecimal orderTaxAmount) {
        // Order tax amount
        if (orderTaxAmount != null && orderTaxAmount.doubleValue() > 0
                && orderTaxAmount.doubleValue() == klarnaRequest.getTotalTaxAmounts().doubleValue()) {
            return true;
        } else if (orderTaxAmount == null) {
            return true;
        } else {
            error = new GenesisError(ErrorMessages.INVALID_TOTAL_TAX_AMOUNTS);
            return false;
        }
    }

    public Boolean isValidAddress(PaymentAddress address) {
        // Init Required Params Validator
        requiredParametersValidator = new RequiredParametersValidator(requiredParameters
                .getRequiredParametersForAddress(address));

        if (!requiredParametersValidator.isValidRequiredParams()) {
            return false;
        } else {
            return true;
        }
    }

    protected Boolean isValidRegex(PaymentRequest request) {
        Boolean isValidAmount = validateAmount(request.getAmount());
        Boolean isValidEmail = validateEmail(request.getCustomerEmail());
        Boolean isValidPhone = validatePhone(request.getCustomerPhone());
        Boolean isValidUrl = validateNotificationUrl(request.getNotificationUrl());

        if (isValidAmount && isValidEmail && isValidPhone && isValidUrl) {
            return true;
        } else {
            return false;
        }
    }

    public GenesisError getError() {
        return error;
    }
}
