package com.emerchantpay.gateway.genesisandroid.api.models.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2ControlChallengeIndicator
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2ControlChallengeWindowSize
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2ControlDeviceType

data class ThreeDsV2ControlParams(val deviceType: ThreeDsV2ControlDeviceType,
                                  val challengeWindowSize: ThreeDsV2ControlChallengeWindowSize? = null,
                                  val challengeIndicator: ThreeDsV2ControlChallengeIndicator? = null)