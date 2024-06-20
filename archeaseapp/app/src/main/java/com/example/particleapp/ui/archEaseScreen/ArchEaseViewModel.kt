package com.example.particleapp.ui.archEaseScreen

import androidx.lifecycle.ViewModel
import cash.z.ecc.android.bip39.Mnemonics
import com.particle.base.ParticleNetwork
import com.particle.base.data.ErrorInfo
import com.particle.base.data.WebOutput
import com.particle.base.data.WebServiceCallback
import com.particle.base.model.ResultCallback
import com.particle.base.model.UserInfo
import com.particle.network.ParticleNetworkAuth.getAddress
import com.particle.network.ParticleNetworkAuth.getUserInfo
import com.particle.network.ParticleNetworkAuth.logout
import com.particle.network.ParticleNetworkAuth.switchChain
import com.example.particleapp.data.QrCodeData
import network.particle.chains.ChainInfo

class ArchEaseViewModel : ViewModel() {
    var paymentSuccess: Boolean = false
    var paymentData = QrCodeData(
        address = "",
        amount = "0",
        chainId = ParticleNetwork.chainId.toString(),
        chainName = ParticleNetwork.chainName
    )
    // Print the generated mnemonic

    fun login(
        onLoginSuccessful: () -> Unit,
        onLoginFailed: (String) -> Unit,
        context: android.content.Context
    ) {
        val sharedPref = context.getSharedPreferences("MySharedPrefs", android.content.Context.MODE_PRIVATE)
        if (sharedPref.getString("mnemonic", "")!!.isEmpty()) {
            val mnemonic = Mnemonics.MnemonicCode(Mnemonics.WordCount.COUNT_24).toList().joinToString(" ")
            val editor = sharedPref.edit()
            editor.putString("mnemonic", "census menu cousin energy kingdom economy egg little erupt bright bean burger cushion crazy pull brother outdoor estate ritual size cattle casino approve material")
            editor.apply()
        }
        println("mnemonic: ${sharedPref.getString("mnemonic", "")}")
        onLoginSuccessful()
    }
    fun logout(onLogoutSuccessful : () -> Unit, onLogoutFailed : (String) -> Unit) {
        ParticleNetwork.logout(object : WebServiceCallback<WebOutput> {
            override fun success(output: WebOutput) {
                onLogoutSuccessful()
            }

            override fun failure(errMsg: ErrorInfo) {
                onLogoutFailed(errMsg.message)
            }
        })
    }

    fun switchChain(chainInfo: ChainInfo, onSuccess: () -> Unit, onFailure: () -> Unit) {
        ParticleNetwork.switchChain(
            chainInfo = chainInfo,
            callback = object : ResultCallback {
                override fun success() {
                    onSuccess()
                }

                override fun failure() {
                    onFailure()
                }
            }
        )
    }

    private fun getUserInfo() : UserInfo? = ParticleNetwork.getUserInfo()
    fun avatar() = ParticleNetwork.getUserInfo()?.avatar

    fun userLoginEmail(): String? {
        var email : String? = ""
        if (ParticleNetwork.getUserInfo()?.email != null)
            email = ParticleNetwork.getUserInfo()?.email
        else if (ParticleNetwork.getUserInfo()?.phone != null)
            email = ParticleNetwork.getUserInfo()?.phone
        else if (ParticleNetwork.getUserInfo()?.googleEmail != null)
            email = ParticleNetwork.getUserInfo()?.googleEmail
        else if (ParticleNetwork.getUserInfo()?.facebookEmail != null)
            email = ParticleNetwork.getUserInfo()?.facebookEmail
        return email
    }

    fun address() = ParticleNetwork.getAddress()

    fun particleNetwork() = ParticleNetwork
}