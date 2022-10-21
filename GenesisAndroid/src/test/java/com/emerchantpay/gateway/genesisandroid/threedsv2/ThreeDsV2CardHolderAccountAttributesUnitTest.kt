package com.emerchantpay.gateway.genesisandroid.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.ThreeDsV2CardHolderAccountAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import java.text.SimpleDateFormat
import java.util.*

internal class ThreeDsV2CardHolderAccountAttributesUnitTest : ThreeDsV2CardHolderAccountAttributes {
    @Test
    @DisplayName("Given attributes with assigned values, When serialized to XML, Then should be valid")
    fun testValidXmlIsGeneratedOnSerialization() {
        setCreationDate(CREATION_DATE)
        setUpdateIndicator(UPDATE_INDICATOR)
        setLastChangeDate(LAST_CHANGE_DATE)
        setPasswordChangeIndicator(PASSWORD_CHANGE_INDICATOR)
        setPasswordChangeDate(PASSWORD_CHANGE_DATE)
        setShippingAddressUsageIndicator(SHIPPING_ADDRESS_USAGE_INDICATOR)
        setShippingAddressDateFirstUsed(SHIPPING_ADDRESS_DATE_FIRST_USED)
        setTransactionsActivityLast24Hours(TRANSACTIONS_ACTIVITY_LAST_24_HOURS)
        setTransactionsActivityPreviousYear(TRANSACTIONS_ACTIVITY_PREVIOUS_YEAR)
        setProvisionAttemptsLast24Hours(PROVISION_ATTEMPTS_LAST_24_HOURS)
        setPurchasesCountLast6Months(PURCHASES_COUNT_LAST_6_MONTHS)
        setSuspiciousActivityIndicator(SUSPICIOUS_ACTIVITY_INDICATOR)
        setRegistrationIndicator(REGISTRATION_INDICATOR)
        setRegistrationDate(REGISTRATION_DATE)

        val generatedXml = buildCardHolderAccountAttributes().toXML()

        assertEquals(EXPECTED_XML, generatedXml)
    }

    companion object {
        private const val CREATION_DATE_TEXT = "14-02-2018"
        private val CREATION_DATE = SimpleDateFormat("dd-MM-yyyy").parse(CREATION_DATE_TEXT)
        private val UPDATE_INDICATOR = ThreeDsV2CardHolderAccountUpdateIndicator.UPDATE_LESS_THAN_30DAYS
        private const val LAST_CHANGE_DATE_TEXT = "29-09-2021"
        private val LAST_CHANGE_DATE = SimpleDateFormat("dd-MM-yyyy").parse(LAST_CHANGE_DATE_TEXT)
        private val PASSWORD_CHANGE_INDICATOR = ThreeDsV2CardHolderAccountPasswordChangeIndicator.PASSWORD_CHANGE_DURING_TRANSACTION
        private const val PASSWORD_CHANGE_DATE_TEXT = "05-01-2022"
        private val PASSWORD_CHANGE_DATE = SimpleDateFormat("dd-MM-yyyy").parse(PASSWORD_CHANGE_DATE_TEXT)
        private val SHIPPING_ADDRESS_USAGE_INDICATOR = ThreeDsV2CardHolderAccountShippingAddressUsageIndicator.ADDRESS_USAGE_MORE_THAN_60DAYS
        private const val SHIPPING_ADDRESS_DATE_FIRST_USED_TEXT = "19-11-2015"
        private val SHIPPING_ADDRESS_DATE_FIRST_USED = SimpleDateFormat("dd-MM-yyyy").parse(SHIPPING_ADDRESS_DATE_FIRST_USED_TEXT)
        private const val TRANSACTIONS_ACTIVITY_LAST_24_HOURS = 2
        private const val TRANSACTIONS_ACTIVITY_PREVIOUS_YEAR = 129
        private const val PROVISION_ATTEMPTS_LAST_24_HOURS = 3
        private const val PURCHASES_COUNT_LAST_6_MONTHS = 111
        private val SUSPICIOUS_ACTIVITY_INDICATOR = ThreeDsV2CardHolderAccountSuspiciousActivityIndicator.NO_SUSPICIOUS_OBSERVED
        private val REGISTRATION_INDICATOR = ThreeDsV2CardHolderAccountRegistrationIndicator.REGISTRATION_GUEST_CHECKOUT
        private const val REGISTRATION_DATE_TEXT = "14-11-2015"
        private val REGISTRATION_DATE = SimpleDateFormat("dd-MM-yyyy").parse(REGISTRATION_DATE_TEXT)

        private val EXPECTED_XML =
            "<creation_date>$CREATION_DATE_TEXT</creation_date>" +
                    "<update_indicator>$UPDATE_INDICATOR</update_indicator>" +
                    "<last_change_date>$LAST_CHANGE_DATE_TEXT</last_change_date>" +
                    "<password_change_indicator>$PASSWORD_CHANGE_INDICATOR</password_change_indicator>" +
                    "<password_change_date>$PASSWORD_CHANGE_DATE_TEXT</password_change_date>" +
                    "<shipping_address_usage_indicator>$SHIPPING_ADDRESS_USAGE_INDICATOR</shipping_address_usage_indicator>" +
                    "<shipping_address_date_first_used>$SHIPPING_ADDRESS_DATE_FIRST_USED_TEXT</shipping_address_date_first_used>" +
                    "<transactions_activity_last_24_hours>$TRANSACTIONS_ACTIVITY_LAST_24_HOURS</transactions_activity_last_24_hours>" +
                    "<transactions_activity_previous_year>$TRANSACTIONS_ACTIVITY_PREVIOUS_YEAR</transactions_activity_previous_year>" +
                    "<provision_attempts_last_24_hours>$PROVISION_ATTEMPTS_LAST_24_HOURS</provision_attempts_last_24_hours>" +
                    "<purchases_count_last_6_months>$PURCHASES_COUNT_LAST_6_MONTHS</purchases_count_last_6_months>" +
                    "<suspicious_activity_indicator>$SUSPICIOUS_ACTIVITY_INDICATOR</suspicious_activity_indicator>" +
                    "<registration_indicator>$REGISTRATION_INDICATOR</registration_indicator>" +
                    "<registration_date>$REGISTRATION_DATE_TEXT</registration_date>"
    }
}