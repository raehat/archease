package com.example.particleapp.utils

import com.example.particleapp.data.BalanceResponse
import com.example.particleapp.data.BankDetails
import com.example.particleapp.data.ClaimRevertPayment
import com.example.particleapp.data.GetAddr
import com.example.particleapp.data.GetAddrResponse
import com.example.particleapp.data.GetReceivedReq
import com.example.particleapp.data.GetSentReq
import com.example.particleapp.data.Order
import com.example.particleapp.data.Payment
import com.example.particleapp.data.PaymentResponse
import com.example.particleapp.data.SendPaymentR
import com.example.particleapp.data.SentPaymentResp
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.encoding.ExperimentalEncodingApi

object Network {

    val backendUrl = "http://localhost:3000"
//    suspend fun fetchReceivedPayments(receiverAddress: String): List<Payment> {
//        val client = HttpClient()
//
//        return try {
//            val url = "https://http-nodejs-vkx8.vercel.app/receivedPayments/$receiverAddress"
//            val _response = client.get(url)
//            val wrappedStringJson = """
//                {
//                  "payments": ${_response.bodyAsText()}
//                }
//            """.trimIndent()
//            val response = Json.decodeFromString<List<List<String>>>(_response.bodyAsText())
//
//            response
//                .filterNot { it[0] == "0x0000000000000000000000000000000000000000" }
//                .map {
//                    Payment(
//                        sender = it[0],
//                        receiver = it[1],
//                        amount = it[3],
//                        deadline = it[4],
//                        claimed = it[5] != "0",
//                        reverted = it[6] != "0",
//                        paymentId = it[7]
//                    )
//                }
//        } finally {
//            client.close()
//        }
//    }
//
//    suspend fun fetchSentPayments(senderAddress: String): List<Payment> {
//        val client = HttpClient()
//
//        return try {
//            val url = "https://http-nodejs-vkx8.vercel.app/sentPayments/$senderAddress"
//            val _response = client.get(url)
//            val wrappedStringJson = """
//                {
//                  "payments": ${_response.bodyAsText()}
//                }
//            """.trimIndent()
//            val response = Json.decodeFromString<List<List<String>>>(_response.bodyAsText())
//
//            response
//                .filterNot { it[0] == "0x0000000000000000000000000000000000000000" }
//                .map {
//                    Payment(
//                        sender = it[0],
//                        receiver = it[1],
//                        amount = it[3],
//                        deadline = it[4],
//                        claimed = it[5].toBoolean(),
//                        reverted = it[6].toBoolean(),
//                        paymentId = it[7]
//                    )
//                }
//        } finally {
//            client.close()
//        }
//    }

    @OptIn(ExperimentalEncodingApi::class, InternalAPI::class)
    suspend fun createOrder(
        email: String,
        name: String,
        bankDetails: BankDetails,
        amountAsked: Double
    ) : HttpResponse? {
        try {
            val response = HttpClient().post("$backendUrl/createOrder") {
                body = Json.encodeToString(
                    Order(
                        email,
                        name,
                        bankDetails,
                        amountAsked
                    )
                )
                contentType(ContentType.Application.Json)
            }
            return response

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @OptIn(ExperimentalEncodingApi::class, InternalAPI::class)
    suspend fun sendPaymentR(
        mnemonic: String,
        address: String,
        amount: String
    ) : SentPaymentResp? {
        try {
            val _response = HttpClient().post("https://archb-92e9.vercel.app/sendPayment") {
                body = Json.encodeToString(
                    SendPaymentR(
                        mnemonic,
                        address,
                        amount
                    )
                )
                contentType(ContentType.Application.Json)
            }
            val response = Json.decodeFromString<SentPaymentResp>(_response.bodyAsText())
            return response

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @OptIn(ExperimentalEncodingApi::class, InternalAPI::class)
    suspend fun receivePaymentR(
        mnemonic: String,
        paymentId: String
    ) : HttpResponse? {
        try {
            val response = HttpClient().post("https://archb-92e9.vercel.app/claimPayment") {
                body = Json.encodeToString(
                    ClaimRevertPayment (
                        mnemonic,
                        paymentId
                    )
                )
                contentType(ContentType.Application.Json)
            }
            return response

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @OptIn(ExperimentalEncodingApi::class, InternalAPI::class)
    suspend fun revertPaymentR(
        mnemonic: String,
        paymentId: String
    ) : HttpResponse? {
        try {
            val response = HttpClient().post("https://archb-92e9.vercel.app/revertPayment") {
                body = Json.encodeToString(
                    ClaimRevertPayment (
                        mnemonic,
                        paymentId
                    )
                )
                contentType(ContentType.Application.Json)
            }
            return response

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @OptIn(ExperimentalEncodingApi::class, InternalAPI::class)
    suspend fun getAddr(
        mnemonic: String
    ) : GetAddrResponse? {
        try {
            val _response = HttpClient().post("https://archb-92e9.vercel.app/getAddr") {
                body = Json.encodeToString(
                    GetAddr (
                        mnemonic
                    )
                )
                contentType(ContentType.Application.Json)
            }
            val response = Json.decodeFromString<GetAddrResponse>(_response.bodyAsText())
            return response

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @OptIn(ExperimentalEncodingApi::class, InternalAPI::class)
    suspend fun getBalance(
        address: String
    ) : BalanceResponse? {
        try {
            val _response = HttpClient().get("https://api.constantine.archway.io/cosmos/bank/v1beta1/balances/archway1aw33ujp5nevaz4dm4vslrlvnnl78ssq85nultv2s5lax2kyp3cjqku076t")
            val response = Json.decodeFromString<BalanceResponse>(_response.bodyAsText())
            return response

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @OptIn(ExperimentalEncodingApi::class, InternalAPI::class)
    suspend fun getSentPaymentsR(
        sender: String
    ) : PaymentResponse {
        try {
            val _response = HttpClient().post("https://archb-92e9.vercel.app/get_sent_payments") {
                body = Json.encodeToString(
                    GetSentReq (
                        sender
                    )
                )
                contentType(ContentType.Application.Json)
            }
            val response = Json.decodeFromString<PaymentResponse>(_response.bodyAsText())
            return response

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return PaymentResponse()
    }

    @OptIn(ExperimentalEncodingApi::class, InternalAPI::class)
    suspend fun getReceivedPaymentsR(
        receiver: String
    ) : PaymentResponse {
        try {
            val _response = HttpClient().post("https://archb-92e9.vercel.app/get_received_payments") {
                body = Json.encodeToString(
                    GetReceivedReq (
                        receiver
                    )
                )
                contentType(ContentType.Application.Json)
            }
            val response = Json.decodeFromString<PaymentResponse>(_response.bodyAsText())
            return response

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return PaymentResponse()
    }
}