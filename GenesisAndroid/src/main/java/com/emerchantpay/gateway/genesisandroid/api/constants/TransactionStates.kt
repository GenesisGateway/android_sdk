package com.emerchantpay.gateway.genesisandroid.api.constants

object TransactionStates {

    // Transaction was approved by the schemes and is successful
    const val APPROVED = "approved"

    // Transaction was declined by the schemes or risk management
    const val DECLINED = "declined"

    // The outcome of the transaction could not be determined, e.g. at a timeout
    // situation.
    // Transaction state will eventually change, so make a reconcile after a
    // certain time frame
    const val PENDING = "pending"

    // An asynchronous transaction (3-D secure payment) has been initiated and
    // is waiting for user input.
    // Updates of this state will be sent to the notification url specified in
    // request
    const val PENDING_ASYNC = "pending_async"

    // Transaction is in-progress
    const val IN_PROGRESS = "in_progress"

    // Once an approved transaction is refunded the state changes to refunded
    const val REFUNDED = "refunded"

    // Transaction was authorized, but later the merchant canceled it
    const val VOIDED = "voided"

    // An error has occurred while negotiating with the schemes
    const val ERROR = "error"

    // Transaction failed, but it was not declined
    const val UNSUCCESSFUL = "unsuccessful"

    // WPF initial status
    const val NEW_STATUS = "new_status"

    // WPF timeout has occurred
    const val TIMEOUT = "timeout"

    // Once an approved transaction is chargeback the state changes to change-
    // backed.
    // Chargeback is the state of rejecting an accepted transaction (with funds
    // transferred)
    // by the cardholder or the issuer
    const val CHARGEBACKED = "chargebacked"

    // Once a chargebacked transaction is charged, the state changes to charge-
    // back reversed.
    // Chargeback has been canceled
    const val CHARGEBACKED_REVERSED = "chargebacked_reversed"
}
