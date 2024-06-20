package com.example.particleapp.data

import kotlinx.serialization.Serializable

@Serializable
data class BankDetails(
    val name: String,
    val ifsc: String,
    val accountNumber: String
)

@Serializable
data class Order(
    val tokenProviderEmail: String,
    val tokenProviderName: String,
    val tokenProviderBankDetails: BankDetails,
    val fiatAmountAsked: Double
)

@Serializable
data class SendPaymentR(
    val mnemonic: String,
    val receiver: String,
    val amount: String,
)

@Serializable
data class ClaimRevertPayment(
    val mnemonic: String,
    val payment_id: String
)

@Serializable
data class GetAddr(
    val mnemonic: String
)

@Serializable
data class GetAddrResponse(
    val address: String
)

@Serializable
data class BalanceResponse(
    val balances: List<Balance>,
    val pagination: Pagination
)

@Serializable
data class Balance(
    val denom: String,
    val amount: String
)

@Serializable
data class Pagination(
    val next_key: String?,
    val total: String
)

@Serializable
data class SentPaymentResp(
    val transactionHash: String
)

@Serializable
data class GetSentReq(
    val sender: String
)

@Serializable
data class GetReceivedReq(
    val receiver: String
)