package com.emerchantpay.gateway.genesisandroid.api.internal.validation;

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;

import java.util.ArrayList;
import java.util.HashMap;

public class RequiredParametersValidator extends GenesisValidator {
    private HashMap<String, String> parametersMap;
    private ArrayList<String> missingParamsList = new ArrayList<String>();

    // Genesis error handler
    private GenesisError error;

    public RequiredParametersValidator() {
        super();
    }

    public RequiredParametersValidator(HashMap<String, String> parametersMap) {
        super();

        this.parametersMap = parametersMap;
    }

    public Boolean isValidRequiredParams() {
        // Missing params
        StringBuilder missingParams = new StringBuilder();

        for (String key : parametersMap.keySet()) {
            if (parametersMap.get(key) == null || parametersMap.get(key).isEmpty()) {
                missingParamsList.add(key);
            }
        }

        for (Integer i = 0; i < missingParamsList.size(); i++) {
            if (i == 0) {
                missingParams.append(missingParamsList.get(0));
            } else {
                missingParams.append(", " + missingParamsList.get(i));
            }
        }

        if (missingParamsList.size() > 1) {
            error = new GenesisError(ErrorMessages.REQUIRED_PARAMS, String.valueOf(missingParams));
            return false;
        } else if (!missingParamsList.isEmpty()) {
            error = new GenesisError(ErrorMessages.REQUIRED_PARAM, String.valueOf(missingParams));
            return false;
        } else {
            return true;
        }
    }

    public GenesisError getError() {
        return error;
    }

    public HashMap<String, String> setParameters(HashMap<String, String> parameters) {
        this.parametersMap = parameters;
        return this.parametersMap;
    }
}
