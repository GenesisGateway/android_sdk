package com.emerchantpay.gateway.genesisandroid.api.constants

enum class ErrorCodes private constructor(internal val code: Int?) {

    SUCCESS(0), ERROR(1),
    SYSTEM_ERROR(100),
    MAINTENANCE_ERROR(101),
    AUTHENTICATION_ERROR(110),
    CONFIGURATION_ERROR(120),
    COMMUNICATION_ERROR(200),
    CONNECTION_ERROR(210),
    ACCOUNT_ERROR(220),
    TIMEOUT_ERROR(230),
    RESPONSE_ERROR(240),
    PARSING_ERROR(250),
    INPUT_DATA_ERROR(300),
    INVALID_TRANSACTION_TYPE_ERROR(310),
    INPUT_DATA_MISSING_ERROR(320),
    INPUT_DATA_FORMAT_ERROR(330),
    INPUT_DATA_INVALID_ERROR(340),
    INVALID_XML_ERROR(350),
    INVALID_CONTENT_TYPE_ERROR(360),
    WORKFLOW_ERROR(400),
    REFERENCE_NOT_FOUND_ERROR(410),
    REFERENCE_WORKFLOW_ERROR(420),
    REFERENCE_INVALIDATED_ERROR(430),
    REFERENCE_MISMATCH_ERROR(440),
    DOUBLE_TRANSACTION_ERROR(450),
    TRANSACTION_NOT_FOUND_ERROR(460),
    PROCESSING_ERROR(500),
    TRANSACTION_PENDING_ERROR(530),
    CREDIT_EXCEEDED_ERROR(540),
    RISK_ERROR(600),
    BIN_COUNTRY_CHECK_ERROR(609),
    CARD_BLACKLIST_ERROR(610),
    BIN_BLACKLIST_ERROR(611),
    COUNTRY_BLACKLIST_ERROR(612),
    IP_BLACKLIST_ERROR(613),
    BLACKLIST_ERROR(614),
    CARD_WHITELIST_ERROR(615),
    CARD_LIMIT_EXCEEDED_ERROR(620),
    TERMINAL_LIMIT_EXCEEDED_ERROR(621),
    CONTRACT_LIMIT_EXCEEDED_ERROR(622),
    CARD_VELOCITY_EXCEEDED_ERROR(623),
    CARD_TICKET_SIZE_EXCEEDED_ERROR(624),
    USER_LIMIT_EXCEEDED_ERROR(625),
    MULTIPLE_FAILURE_DETECTION_ERROR(626),
    CS_DETECTION_ERROR(627),
    RECURRING_LIMIT_EXCEEDED_ERROR(628),
    AVS_ERROR(690),
    THREAT_METRIX_RISK_ERROR(692),
    REMOTE_ERROR(900),
    REMOTE_SYSTEM_ERROR(910),
    REMOTE_CONFIGURATION_ERROR(920),
    REMOTE_DATA_ERROR(930),
    REMOTE_WORKFLOW_ERROR(940),
    REMOTE_TIMEOUT_ERROR(950),
    REMOTE_CONNECTION_ERROR(960),
    UNKNOWN_VALIDATION_ERROR(0);

    fun getCode(): Int {
        return code!!
    }

    fun findByCode(code: Int?): ErrorCodes {
        for (errorCode in values()) {
            if (errorCode.code == code) {
                return errorCode
            }
        }
        return UNKNOWN_VALIDATION_ERROR
    }
    
    companion object {
        @JvmStatic
        fun getErrorDescription(code: Int): String {
            when (code) {
                1 -> return "Undefined error."
                100 -> return "A general system error occurred."
                101 -> return "System is undergoing maintenance, request could not be handled."
                110 -> return "Login failed. Check your API credentials."
                120 -> return "Config error occurred, e.g. terminal not configured properly. Check terminal settings."
                200 -> return "Communication with issuer failed, please contact support."
                210 -> return "Connection to issuer could not be established, please contact support."
                220 -> return "Issuer account data invalid, please contact support."
                230 -> return "Issuer does not respond within given time-frame - please reconcile."
                240 -> return "Issuer returned invalid response - please reconcile and contact support."
                250 -> return "Issuer response could not be parsed - please reconcile and contact support."
                300 -> return "Invalid were data sent to the API."
                310 -> return "Invalid transaction type was passed to API."
                320 -> return "Required argument is missing."
                330 -> return "Argument passed in invalid format."
                340 -> return "Argument passed in valid format but makes no sense (e.g. incorrect country code or currency)."
                350 -> return "The input Builder could not be parsed due to invalid code."
                400 -> return "A transaction was triggered that is not possible at this time in the workflow, e.g. a refund on a declined transaction."
                410 -> return "Reference transaction was not found."
                420 -> return "Wrong Workflow specified."
                430 -> return "Reference transaction already invalidated!"
                440 -> return "Data mismatch with reference, e.g. amount exceeds reference."
                450 -> return "Transaction doublet was detected, transaction was blocked."
                460 -> return "The referenced transaction could not be found."
                500 -> return "Transaction declined by issuer."
                510 -> return "Transaction declined, Credit card number is invalid."
                520 -> return "Transaction declined, expiration date not in the future or date invalid."
                530 -> return "Transaction pending."
                540 -> return "Amount exceeds credit card limit."
                600 -> return "Transaction declined by risk management."
                609 -> return "Card bin does not match billing country."
                610 -> return "Card is blacklisted."
                611 -> return "BIN blacklisted."
                612 -> return "Country blacklisted."
                613 -> return "IP address blacklisted."
                614 -> return "Value from the Transaction Request or Risk Parameters is blacklisted."
                616 -> return "PAN Whitelist Filter blocked the transaction."
                620 -> return "Card limit exceeded configured limits."
                621 -> return "Terminal limits exceeded."
                622 -> return "MID limits exceeded."
                623 -> return "Velocity by unknown card exceeded!"
                624 -> return "Ticket size by unknown card exceeded!"
                625 -> return "User limit exceeded configured limits."
                626 -> return "Found user transaction declined by issuer."
                627 -> return "CrossSelling Error!"
                628 -> return "Amount/count by recurring subscription exceeded."
                629 -> return "PaymentAddress Verification failed."
                691 -> return "MaxMind High Risk Error."
                692 -> return "ThreatMetrix High Risk Error."
                900 -> return "Some error occurred on the issuer."
                910 -> return "Some error occurred on the issuer."
                920 -> return "Issuer configuration error."
                930 -> return "Some passed data caused an error on the issuer."
                940 -> return "Remote workflow error."
                950 -> return "Issuer has time-out with clearing network."
                960 -> return "Issuer could not reach clearing network."
                else -> return "Undefined error."
            }
        }

        @JvmStatic
        fun getIssuerResponseCode(issuerResponseCode: String): String {
            when (issuerResponseCode) {
                "00" -> return "Approved or completed successfully"
                "02" -> return "Refer to card issue"
                "03" -> return "Invalid merchant"
                "04", "05" -> return "Do not honour"
                "06" -> return "Invalid Transaction for Terminal"
                "07" -> return "Honour with ID"
                "08" -> return "Time-Out"
                "09" -> return "No Original"
                "10" -> return "Unable to Reverse"
                "11" -> return "Partial Approval"
                "12" -> return "Invalid transaction card / issuer / acquirer"
                "13" -> return "Invalid amount"
                "14" -> return "Invalid card number"
                "17" -> return "Invalid Capture date (terminal business date)"
                "19" -> return "System Error; Re-enter transaction"
                "20" -> return "No From Account"
                "21" -> return "No To Account"
                "22" -> return "No Checking Account"
                "23" -> return "No Saving Account"
                "24" -> return "No Credit Account"
                "30" -> return "Format error"
                "34" -> return "Implausible card data"
                "39" -> return "Transaction Not Allowed"
                "41" -> return "Pick-up card"
                "42" -> return "Special Pickup"
                "43" -> return "Hot Card, Pickup (if possible)"
                "44" -> return "Pickup Card"
                "45" -> return "Transaction Back Off"
                "51" -> return "Not sufficient funds"
                "54" -> return "Expired card"
                "55" -> return "Incorrect PIN; Re-enter"
                "57" -> return "Not permitted on card"
                "58" -> return "Txn Not Permitted On Term"
                "61" -> return "Exceeds amount limit"
                "62" -> return "Restricted card"
                "63" -> return "MAC Key Error"
                "65" -> return "Exceeds frequency limit"
                "66" -> return "Exceeds Acquirer Limit"
                "67" -> return "Retain Card; no reason specified"
                "68" -> return "Response received too late"
                "75" -> return "Exceeds PIN Retry"
                "76" -> return "Invalid Account"
                "77" -> return "Issuer Does Not Participate In The Service"
                "78" -> return "Function Not Available"
                "79" -> return "Key Validation Error"
                "80" -> return "Approval for Purchase Amount Only"
                "81" -> return "Unable to Verify PIN"
                "82" -> return "Invalid Card Verification Value"
                "83" -> return "Not declined (AVS Only)"
                "84" -> return "Invalid Life Cycle of transaction"
                "85" -> return "No Keys To Use"
                "86" -> return "K M E Sync Error"
                "87" -> return "PIN Key Error"
                "88" -> return "MAC sync Error"
                "89" -> return "Security Violation"
                "91" -> return "Issuer not available"
                "92" -> return "Invalid Issuer"
                "93" -> return "Transaction cannot be completed"
                "94" -> return "Invalid originator"
                "96" -> return "System malfunction"
                "97" -> return "No Funds Transfer"
                "98" -> return "Duplicate Reversal"
                "99" -> return "Duplicate Transaction"
                "N3" -> return "Cash Service Not Available"
                "N4" -> return "Cash Back Request Exceeds Issuer Limit"
                "N7" -> return "CVV2 Failure"
                "R0" -> return "Stop Payment Order"
                "R1" -> return "Revocation of Authorisation Order"
                "R3" -> return "Revocation of all Authorisations Order"
                else -> return "Approved or completed successfully"
            }
        }
    }
}
