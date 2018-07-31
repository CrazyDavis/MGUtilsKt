package org.magicalwater.mgkotlin.mgutilskt.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo


class MGNetworkStatusUtils {

    var networkDetectHandler: ((Boolean) -> Unit)? = null

    var connectionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            networkDetectHandler?.invoke(isNetworkConnected(context))
        }
    }

    //網路狀態是否連接
    fun isNetworkConnected(context: Context): Boolean {
        val connectMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return (mobNetInfo.isConnected || wifiNetInfo.isConnected)
    }

    //註冊網路狀態檢測回調
    fun registerConnectionReceiver(context: Context, statusHandler: (Boolean) -> Unit) {
        unregisterConnectionReceiver(context)
        networkDetectHandler = statusHandler

        var intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(connectionReceiver, intentFilter)
    }

    fun unregisterConnectionReceiver(context: Context) {
        if (networkDetectHandler != null) {
            context.unregisterReceiver(connectionReceiver)
            networkDetectHandler = null
        }
    }

}