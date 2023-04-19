package com.albrodiaz.gestvet.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

sealed class ConnectionState {
    object Available: ConnectionState()
    object Unavailable: ConnectionState()
}

class NetworkConnectivity(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun getConnectivity() = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch { send(ConnectionState.Available) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(ConnectionState.Unavailable) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                launch { send(ConnectionState.Unavailable) }
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}
