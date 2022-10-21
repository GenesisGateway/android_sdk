package com.emerchantpay.gateway.genesisandroid.api.models.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.*
import java.util.*

data class ThreeDsV2CardHolderAccountParams(val creationDate: Date? = null,
                                            val updateIndicator: ThreeDsV2CardHolderAccountUpdateIndicator? = null,
                                            val lastChangeDate: Date? = null,
                                            val passwordChangeIndicator: ThreeDsV2CardHolderAccountPasswordChangeIndicator? = null,
                                            val passwordChangeDate: Date? = null,
                                            val shippingAddressUsageIndicator: ThreeDsV2CardHolderAccountShippingAddressUsageIndicator? = null,
                                            val shippingAddressDateFirstUsed: Date? = null,
                                            val transactionsActivityLast24Hours: Int? = null,
                                            val transactionsActivityPreviousYear: Int? = null,
                                            val provisionAttemptsLast24Hours: Int? = null,
                                            val purchasesCountLast6Months: Int? = null,
                                            val suspiciousActivityIndicator: ThreeDsV2CardHolderAccountSuspiciousActivityIndicator? = null,
                                            val registrationIndicator: ThreeDsV2CardHolderAccountRegistrationIndicator? = null,
                                            val registrationDate: Date? = null)
