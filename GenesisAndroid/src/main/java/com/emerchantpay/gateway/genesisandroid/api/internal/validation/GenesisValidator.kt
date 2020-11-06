package com.emerchantpay.gateway.genesisandroid.api.internal.validation

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages
import com.emerchantpay.gateway.genesisandroid.api.constants.ReminderConstants
import com.emerchantpay.gateway.genesisandroid.api.constants.WPFTransactionTypes
import com.emerchantpay.gateway.genesisandroid.api.internal.request.KlarnaItemsRequest
import com.emerchantpay.gateway.genesisandroid.api.internal.request.PaymentRequest
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.models.PaymentAddress
import com.emerchantpay.gateway.genesisandroid.api.models.Reminder
import java.math.BigDecimal
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

open class GenesisValidator {

    private val notValidParamsList = ArrayList<String>()
    private val emptyParamsList = ArrayList<String>()

    // Genesis error handler
    open var error: GenesisError? = null
        internal set

    // Required params validator
    private val requiredParameters = RequiredParameters()
    private var requiredParametersValidator: RequiredParametersValidator? = null

    // Validate amount
    fun validateAmount(amount: BigDecimal?): Boolean? {
        return when {
            amount!!.toDouble() > 0 && amount != null -> true
            else -> {
                error = GenesisError(ErrorMessages.INVALID_AMOUNT)
                notValidParamsList.add(amount.toString())

                false
            }
        }
    }

    fun validateEmail(email: String?): Boolean? {
        val m = VALID_EMAIL_REGEX.matcher(email!!)

        return when {
            m.matches() && email != null && !email.isEmpty() -> true
            else -> {
                error = GenesisError(ErrorMessages.INVALID_EMAIL)
                notValidParamsList.add(email)

                false
            }
        }
    }

    fun validatePhone(phone: String?): Boolean? {
        val m = VALID_PHONE_REGEX.matcher(phone!!)

        if (m.matches() && phone != null && !phone.isEmpty()) {
            return true
        } else {
            error = GenesisError(ErrorMessages.INVALID_PHONE)
            notValidParamsList.add(phone)

            return false
        }
    }

    // Validate Url
    fun validateNotificationUrl(url: String?): Boolean? {
        var m: Matcher? = null

        when {
            url != null -> m = VALID_URL_REGEX.matcher(url)
        }

        return when {
            m != null && m.matches() && url != null && !url.isEmpty() -> true
            else -> {
                error = GenesisError(ErrorMessages.INVALID_NOTIFICATION_URL)
                url?.let { notValidParamsList.add(it) }

                false
            }
        }
    }

    @Throws(IllegalAccessException::class)
    fun validateTransactionType(transactionType: String): Boolean? {
        val fields = WPFTransactionTypes::class.java.enumConstants

        val isMatch = fields?.any { field ->
            transactionType.toLowerCase(Locale.ROOT) == field.value.toLowerCase(Locale.ROOT)
        } ?: false

        if (!isMatch) {
            error = GenesisError(ErrorMessages.INVALID_TRANSACTION_TYPE, transactionType)
            notValidParamsList.add(transactionType)
        }

        return isMatch
    }

    fun validateString(param: String, value: String?): Boolean? {
        return when {
            value == null || value.isEmpty() || value == "null" -> {

                error = GenesisError(ErrorMessages.EMPTY_PARAM, param)
                if (!emptyParamsList.contains(param)) {
                    emptyParamsList.add(param)
                }

                false
            }
            else -> true
        }
    }

    fun isValidRequest(request: PaymentRequest): Boolean? {

        requiredParametersValidator = RequiredParametersValidator(requiredParameters
                .getRequiredParametersForRequest(request))

        when {
            !requiredParametersValidator!!.isValidRequiredParams!! && notValidParamsList != null
                    && !notValidParamsList.isEmpty() -> {
                error = requiredParametersValidator!!.error
                return false
            }
            else -> {
                requiredParametersValidator = RequiredParametersValidator()

                request.transactionTypes.transactionTypesList.forEach { transactionType ->
                    requiredParametersValidator!!.setParameters(requiredParameters.getRequiredParametersForTransactionType(request, transactionType))
                    when {
                        !requiredParametersValidator!!.isValidRequiredParams!! -> {
                            val errorMessage = requiredParametersValidator!!.error!!.message
                            val errorTechnicalMessage = requiredParametersValidator!!.error!!.technicalMessage
                            error = GenesisError("is required for $transactionType transaction type",
                                    "$errorTechnicalMessage parameter ")
                        }
                    }
                }

                return when {
                    !requiredParametersValidator!!.isValidRequiredParams!! -> false
                    else -> isValidRegex(request)!!
                }
            }
        }
    }

    fun isValidKlarnaRequest(klarnaRequest: KlarnaItemsRequest?, transactionAmount: BigDecimal,
                             orderTaxAmount: BigDecimal?): Boolean? {
        when {
            klarnaRequest != null -> requiredParametersValidator = RequiredParametersValidator(requiredParameters
                    .getRequiredParametersForKlarnaItem(klarnaRequest))
            else -> {
                error = GenesisError(ErrorMessages.REQUIRED_PARAMS, "items")
                return false
            }
        }

        return (requiredParametersValidator!!.isValidRequiredParams!! && validateTransactionAmount(klarnaRequest, transactionAmount)!! && validateOrderTaxAmount(klarnaRequest, orderTaxAmount)!!)
    }

    protected fun validateTransactionAmount(klarnaRequest: KlarnaItemsRequest, transactionAmount: BigDecimal?): Boolean? {
        // Transaction amount
        return when {
            transactionAmount != null
                    && transactionAmount.toDouble() > 0
                    && klarnaRequest.totalAmounts.toDouble() > 0
                    && transactionAmount.toDouble() == klarnaRequest.totalAmounts.toDouble() -> true
            else -> {
                error = GenesisError(ErrorMessages.INVALID_TOTAL_AMOUNTS)
                false
            }
        }
    }

    protected fun validateOrderTaxAmount(klarnaRequest: KlarnaItemsRequest, orderTaxAmount: BigDecimal?): Boolean? {
        // Order tax amount
        return when {
            orderTaxAmount != null && orderTaxAmount.toDouble() > 0
                    && orderTaxAmount.toDouble() == klarnaRequest.totalTaxAmounts.toDouble() -> true
            orderTaxAmount == null -> true
            else -> {
                error = GenesisError(ErrorMessages.INVALID_TOTAL_TAX_AMOUNTS)
                false
            }
        }
    }

    fun isValidAddress(address: PaymentAddress): Boolean? {
        // Init Required Params Validator
        requiredParametersValidator = RequiredParametersValidator(requiredParameters
                .getRequiredParametersForAddress(address))

        return requiredParametersValidator!!.isValidRequiredParams!!
    }

    fun validateReminder(channel: String, after: Int?): Boolean? {
        if (channel !== ReminderConstants.REMINDERS_CHANNEL_EMAIL && channel !== ReminderConstants.REMINDERS_CHANNEL_SMS) {
            error = GenesisError(ErrorMessages.INVALID_CHANNEL
                    + ReminderConstants.REMINDERS_CHANNEL_EMAIL
                    + ", " + ReminderConstants.REMINDERS_CHANNEL_SMS)
            return false
        } else if (after!! < ReminderConstants.MIN_ALLOWED_REMINDER_MINUTES || after > ReminderConstants.MAX_ALLOWED_REMINDER_DAYS * 24 * 60) {
            error = GenesisError(ErrorMessages.INVALID_REMINDER_AFTER)
            return false
        }
        return true
    }

    fun validateRemindersNumber(reminders: ArrayList<Reminder>): Boolean? {
        return when {
            reminders.size > 3 -> {
                error = GenesisError(ErrorMessages.INVALID_REMINDERS_NUMBER)
                false
            }
            else -> true
        }
    }

    fun isValidConsumerId(consumerId: String?): Boolean? {
        return when {
            consumerId == null || consumerId.length > 10 || !consumerId.matches("[0-9]+".toRegex()) -> {
                error = GenesisError(ErrorMessages.INVALID_CONSUMER_ID)
                false
            }
            else -> true
        }
    }

    private fun isValidRegex(request: PaymentRequest): Boolean? {
        val isValidAmount = validateAmount(request.amount)
        val isValidEmail = validateEmail(request.customerEmail)
        val isValidPhone = validatePhone(request.customerPhone)
        val isValidUrl = validateNotificationUrl(request.notificationUrl)

        return isValidAmount!! && isValidEmail!! && isValidPhone!! && isValidUrl!!
    }

    companion object {
        // Regular expressions
        val VALID_EMAIL_REGEX = Pattern
                .compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}", Pattern.CASE_INSENSITIVE)
        val VALID_PHONE_REGEX = Pattern.compile("^[0-9\\+]{1,}[0-9\\\\-]{3,15}$")
        val VALID_URL_REGEX = Pattern
                .compile("\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
    }
}
