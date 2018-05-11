package com.emerchantpay.gateway.genesisandroid.api.constants;

public class TransactionStates {

	// Transaction was approved by the schemes and is successful
	public static String APPROVED = "approved";

	// Transaction was declined by the schemes or risk management
	public static String DECLINED = "declined";

	// The outcome of the transaction could not be determined, e.g. at a timeout
	// situation.
	// Transaction state will eventually change, so make a reconcile after a
	// certain time frame
	public static String PENDING = "pending";

	// An asynchronous transaction (3-D secure payment) has been initiated and
	// is waiting for user input.
	// Updates of this state will be sent to the notification url specified in
	// request
	public static String PENDING_ASYNC = "pending_async";

	// Transaction is in-progress
	public static String IN_PROGRESS = "in_progress";

	// Once an approved transaction is refunded the state changes to refunded
	public static String REFUNDED = "refunded";

	// Transaction was authorized, but later the merchant canceled it
	public static String VOIDED = "voided";

	// An error has occurred while negotiating with the schemes
	public static String ERROR = "error";

	// Transaction failed, but it was not declined
	public static String UNSUCCESSFUL = "unsuccessful";

	// WPF initial status
	public static String NEW_STATUS = "new_status";

	// WPF timeout has occurred
	public static String TIMEOUT = "timeout";

	// Once an approved transaction is chargeback the state changes to change-
	// backed.
	// Chargeback is the state of rejecting an accepted transaction (with funds
	// transferred)
	// by the cardholder or the issuer
	public static String CHARGEBACKED = "chargebacked";

	// Once a chargebacked transaction is charged, the state changes to charge-
	// back reversed.
	// Chargeback has been canceled
	public static String CHARGEBACKED_REVERSED = "chargebacked_reversed";
}
