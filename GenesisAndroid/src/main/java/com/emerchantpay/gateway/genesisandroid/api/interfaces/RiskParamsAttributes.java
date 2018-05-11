package com.emerchantpay.gateway.genesisandroid.api.interfaces;

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder;

public interface RiskParamsAttributes {

    RequestBuilder requestBuilder = new RequestBuilder("");

    // Risk Params
    default RiskParamsAttributes setRiskSSN(String ssn) {
        requestBuilder.addElement("ssn", ssn);
        return this;
    }

    default RiskParamsAttributes setRiskMacAddress(String macAddress) {
        requestBuilder.addElement("mac_address", macAddress);
        return this;
    }

    default RiskParamsAttributes setRiskSessionId(String sessionId) {
        requestBuilder.addElement("session_id", sessionId);
        return this;
    }

    default RiskParamsAttributes setRiskUserId(String userId) {
        requestBuilder.addElement("user_id", userId);
        return this;
    }

    default RiskParamsAttributes setRiskUserLevel(String userLevel) {
        requestBuilder.addElement("user_level", userLevel);
        return this;
    }

    default RiskParamsAttributes setRiskEmail(String email) {
        requestBuilder.addElement("email", email);
        return this;
    }

    default RiskParamsAttributes setRiskPhone(String phone) {
        requestBuilder.addElement("phone", phone);
        return this;
    }

    default RiskParamsAttributes setRiskRemoteIp(String remoteIp) {
        requestBuilder.addElement("remote_ip", remoteIp);
        return this;
    }

    default RiskParamsAttributes setRiskSerialNumber(String serialNumber) {
        requestBuilder.addElement("serial_number", serialNumber);
        return this;
    }

    default RiskParamsAttributes setRiskPanTail(String panTail) {
        requestBuilder.addElement("pan_tail", panTail);
        return this;
    }

    default RiskParamsAttributes setRiskBin(String bin) {
        requestBuilder.addElement("bin", bin);
        return this;
    }

    default RiskParamsAttributes setRiskFirstName(String firstname) {
        requestBuilder.addElement("first_name", firstname);
        return this;
    }

    default RiskParamsAttributes setRiskLastName(String lastname) {
        requestBuilder.addElement("last_name", lastname);
        return this;
    }

    default RiskParamsAttributes setRiskCountry(String country) {
        requestBuilder.addElement("country", country);
        return this;
    }

    default RiskParamsAttributes setRiskPan(String pan) {
        requestBuilder.addElement("pan", pan);
        return this;
    }

    default RiskParamsAttributes setRiskForwardedIp(String forwardedIp) {
        requestBuilder.addElement("forwarded_ip", forwardedIp);
        return this;
    }

    default RiskParamsAttributes setRiskUsername(String username) {
        requestBuilder.addElement("username", username);
        return this;
    }

    default RiskParamsAttributes setRiskPassword(String password) {
        requestBuilder.addElement("password", password);
        return this;
    }

    default RiskParamsAttributes setRiskBinName(String binName) {
        requestBuilder.addElement("bin_name", binName);
        return this;
    }

    default RiskParamsAttributes setRiskBinPhone(String binPhone) {
        requestBuilder.addElement("bin_phone", binPhone);
        return this;
    }

    default RequestBuilder buildRiskParams() {
        return requestBuilder;
    }
}