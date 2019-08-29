package com.emerchantpay.gateway.genesisandroid.api.internal.request

import com.emerchantpay.gateway.genesisandroid.api.interfaces.ReminderAttributes
import com.emerchantpay.gateway.genesisandroid.api.internal.validation.GenesisValidator
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.models.Reminder
import com.emerchantpay.gateway.genesisandroid.api.util.Request
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder
import java.util.*

class RemindersRequest : Request, ReminderAttributes {

    private var parent: PaymentRequest? = null

    // Genesis Validator
    private val validator = GenesisValidator()

    var remindersList = ArrayList<Reminder>()
        private set

    val error: GenesisError?
        get() = validator.error

    constructor(parent: PaymentRequest) {
        this.parent = parent
    }

    constructor(reminder: Reminder) {
        when {
            parent?.payLater == true
                    && validator.validateReminder(reminder.channel, reminder.after)!!
                    && validator.validateRemindersNumber(remindersList)!! -> this.remindersList.add(reminder)
        }
    }

    fun addReminder(channel: String, after: Int?): RemindersRequest {
        when {
            parent?.payLater == true
                    && validator.validateReminder(channel, after)!!
                    && validator.validateRemindersNumber(remindersList)!! -> remindersList.add(Reminder(channel, after))
        }
        return this
    }

    constructor(remindersList: ArrayList<Reminder>) {
        remindersList.forEach { reminder ->
            when {
                parent?.payLater == true
                        && validator.validateReminder(reminder.channel, reminder.after)!!
                        && validator.validateRemindersNumber(remindersList)!! -> this.remindersList = remindersList
            }
        }
    }

    override fun toXML(): String {
        return buildRequest("reminders").toXML()
    }

    override fun toQueryString(root: String): String {
        return buildRequest(root).toQueryString()
    }


    protected fun buildRequest(root: String): RequestBuilder {
        val builder = RequestBuilder(root)
        remindersList.forEach { reminder ->
            builder.addElement("reminder", setChannel(reminder.channel)
                    .setAfter(reminder.after)
                    .buildReminders().toXML())
        }

        return builder
    }

    fun done(): PaymentRequest? {
        return parent
    }
}
