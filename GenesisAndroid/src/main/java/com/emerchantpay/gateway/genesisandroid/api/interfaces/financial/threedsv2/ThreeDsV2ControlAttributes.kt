package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2ControlChallengeIndicator
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2ControlChallengeWindowSize
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2ControlDeviceType
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface ThreeDsV2ControlAttributes {
    fun setDeviceType(deviceType: ThreeDsV2ControlDeviceType) = apply {
        requestBuilder.addElement("device_type", deviceType)
    }

    fun setChallengeWindowSize(challengeWindowSize: ThreeDsV2ControlChallengeWindowSize?) = apply {
        challengeWindowSize?.let { requestBuilder.addElement("challenge_window_size", it) }
    }

    fun setChallengeIndicator(challengeIndicator: ThreeDsV2ControlChallengeIndicator?) = apply {
        challengeIndicator?.let { requestBuilder.addElement("challenge_indicator", it) }
    }

    fun buildControlAttributes(): RequestBuilder = requestBuilder

    companion object {
        private val requestBuilder = RequestBuilder("")
    }
}