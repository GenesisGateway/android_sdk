package com.emerchantpay.gateway.genesisandroid.api.interfaces

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface RiskParamsAttributes {

    // Risk Params
    fun setRiskSSN(ssn: String): RiskParamsAttributes {
        requestBuilder.addElement("ssn", ssn)
        return this
    }

    fun setRiskMacAddress(macAddress: String): RiskParamsAttributes {
        requestBuilder.addElement("mac_address", macAddress)
        return this
    }

    fun setRiskSessionId(sessionId: String): RiskParamsAttributes {
        requestBuilder.addElement("session_id", sessionId)
        return this
    }

    fun setRiskUserId(userId: String): RiskParamsAttributes {
        requestBuilder.addElement("user_id", userId)
        return this
    }

    fun setRiskUserLevel(userLevel: String): RiskParamsAttributes {
        requestBuilder.addElement("user_level", userLevel)
        return this
    }

    fun setRiskEmail(email: String): RiskParamsAttributes {
        requestBuilder.addElement("email", email)
        return this
    }

    fun setRiskPhone(phone: String): RiskParamsAttributes {
        requestBuilder.addElement("phone", phone)
        return this
    }

    fun setRiskRemoteIp(remoteIp: String): RiskParamsAttributes {
        requestBuilder.addElement("remote_ip", remoteIp)
        return this
    }

    fun setRiskSerialNumber(serialNumber: String): RiskParamsAttributes {
        requestBuilder.addElement("serial_number", serialNumber)
        return this
    }

    fun setRiskPanTail(panTail: String): RiskParamsAttributes {
        requestBuilder.addElement("pan_tail", panTail)
        return this
    }

    fun setRiskBin(bin: String): RiskParamsAttributes {
        requestBuilder.addElement("bin", bin)
        return this
    }

    fun setRiskFirstName(firstname: String): RiskParamsAttributes {
        requestBuilder.addElement("first_name", firstname)
        return this
    }

    fun setRiskLastName(lastname: String): RiskParamsAttributes {
        requestBuilder.addElement("last_name", lastname)
        return this
    }

    fun setRiskCountry(country: String): RiskParamsAttributes {
        requestBuilder.addElement("country", country)
        return this
    }

    fun setRiskPan(pan: String): RiskParamsAttributes {
        requestBuilder.addElement("pan", pan)
        return this
    }

    fun setRiskForwardedIp(forwardedIp: String): RiskParamsAttributes {
        requestBuilder.addElement("forwarded_ip", forwardedIp)
        return this
    }

    fun setRiskUsername(username: String): RiskParamsAttributes {
        requestBuilder.addElement("username", username)
        return this
    }

    fun setRiskPassword(password: String): RiskParamsAttributes {
        requestBuilder.addElement("password", password)
        return this
    }

    fun setRiskBinName(binName: String): RiskParamsAttributes {
        requestBuilder.addElement("bin_name", binName)
        return this
    }

    fun setRiskBinPhone(binPhone: String): RiskParamsAttributes {
        requestBuilder.addElement("bin_phone", binPhone)
        return this
    }

    fun buildRiskParams(): RequestBuilder {
        return requestBuilder
    }

    companion object {

        val requestBuilder = RequestBuilder("")
    }
}