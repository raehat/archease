package com.example.particleapp

import android.app.Application
import com.particle.base.Env
import com.particle.base.ParticleNetwork
import network.particle.chains.ChainInfo

class ArchEaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ParticleNetwork.init(this, Env.DEV, ChainInfo.EthereumSepolia)
    }
}