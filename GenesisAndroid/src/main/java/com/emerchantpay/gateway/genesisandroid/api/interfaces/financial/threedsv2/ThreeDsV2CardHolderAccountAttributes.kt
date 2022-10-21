package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.*
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder
import java.text.SimpleDateFormat
import java.util.*

interface ThreeDsV2CardHolderAccountAttributes {
    fun setCreationDate(creationDate: Date?) = apply {
        creationDate?.let { requestBuilder.addElement("creation_date",  SimpleDateFormat(DATE_FORMAT).format(it)) }
    }

    fun setUpdateIndicator(updateIndicator: ThreeDsV2CardHolderAccountUpdateIndicator?) = apply {
        updateIndicator?.let { requestBuilder.addElement("update_indicator",  it.toString()) }
    }

    fun setLastChangeDate(lastChangeDate: Date?) = apply {
        lastChangeDate?.let { requestBuilder.addElement("last_change_date",  SimpleDateFormat(DATE_FORMAT).format(it)) }
    }

    fun setPasswordChangeIndicator(passwordChangeIndicator: ThreeDsV2CardHolderAccountPasswordChangeIndicator?) = apply {
        passwordChangeIndicator?.let { requestBuilder.addElement("password_change_indicator",  it.toString()) }
    }

    fun setPasswordChangeDate(passwordChangeDate: Date?) = apply {
        passwordChangeDate?.let { requestBuilder.addElement("password_change_date",  SimpleDateFormat(DATE_FORMAT).format(it)) }
    }

    fun setShippingAddressUsageIndicator(shippingAddressUsageIndicator: ThreeDsV2CardHolderAccountShippingAddressUsageIndicator?) = apply {
        shippingAddressUsageIndicator?.let { requestBuilder.addElement("shipping_address_usage_indicator",  it.toString()) }
    }

    fun setShippingAddressDateFirstUsed(shippingAddressDateFirstUsed: Date?) = apply {
        shippingAddressDateFirstUsed?.let { requestBuilder.addElement("shipping_address_date_first_used",  SimpleDateFormat(DATE_FORMAT).format(it)) }
    }

    fun setTransactionsActivityLast24Hours(transactionsActivityLast24Hours: Int?) = apply {
        transactionsActivityLast24Hours?.let { requestBuilder.addElement("transactions_activity_last_24_hours",  it.toString()) }
    }

    fun setTransactionsActivityPreviousYear(transactionsActivityPreviousYear: Int?) = apply {
        transactionsActivityPreviousYear?.let { requestBuilder.addElement("transactions_activity_previous_year",  it.toString()) }
    }

    fun setProvisionAttemptsLast24Hours(provisionAttemptsLast24Hours: Int?) = apply {
        provisionAttemptsLast24Hours?.let { requestBuilder.addElement("provision_attempts_last_24_hours",  it.toString()) }
    }

    fun setPurchasesCountLast6Months(purchasesCountLast6Months: Int?) = apply {
        purchasesCountLast6Months?.let { requestBuilder.addElement("purchases_count_last_6_months",  it.toString()) }
    }

    fun setSuspiciousActivityIndicator(suspiciousActivityIndicator: ThreeDsV2CardHolderAccountSuspiciousActivityIndicator?) = apply {
        suspiciousActivityIndicator?.let { requestBuilder.addElement("suspicious_activity_indicator",  it.name.lowercase()) }
    }

    fun setRegistrationIndicator(registrationIndicator: ThreeDsV2CardHolderAccountRegistrationIndicator?) = apply {
        registrationIndicator?.let { requestBuilder.addElement("registration_indicator",  it.toString()) }
    }

    fun setRegistrationDate(registrationDate: Date?) = apply {
        registrationDate?.let { requestBuilder.addElement("registration_date",  SimpleDateFormat(DATE_FORMAT).format(it)) }
    }

    fun buildCardHolderAccountAttributes(): RequestBuilder = requestBuilder

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy"
        private val requestBuilder = RequestBuilder("")
    }
}