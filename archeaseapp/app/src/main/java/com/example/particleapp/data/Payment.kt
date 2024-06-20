package com.example.particleapp.data

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    val Ok: List<Payment> = listOf()
)

@Serializable
data class Payment(
    val sender: String,
    val receiver: String,
    val amount: Long,
    val amount_in_coins: AmountInCoins,
    val deadline: Long,
    val claimed: Boolean,
    val reverted: Boolean,
    val payment_id: Int
)

@Serializable
data class AmountInCoins(
    val denom: String,
    val amount: String
)