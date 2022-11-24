package com.ssafy.guffy.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import com.ssafy.guffy.util.Common.Companion.isNetworkConnected


class CheckNetwork(val context: Context) {
    /*
    You need to call the below method once. It register the callback and fire it when there is a change in network state.
    Here I used a Global Static Variable, So I can use it to access the network state in anyware of the application.
    */

    // You need to pass the context when creating the class


    // Network Check
    @RequiresApi(Build.VERSION_CODES.N)
    fun registerNetworkCallback() {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder: NetworkRequest.Builder = NetworkRequest.Builder()
            connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isNetworkConnected = true // Global Static Variable
                }

                override fun onLost(network: Network) {
                    isNetworkConnected = false // Global Static Variable
                }
            }
            )
            isNetworkConnected = false
        } catch (e: Exception) {
            isNetworkConnected = false
        }
    }
}