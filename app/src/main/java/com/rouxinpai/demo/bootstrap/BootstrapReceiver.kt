package com.rouxinpai.demo.bootstrap

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkRequest
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/8 10:33
 * desc   :
 */
class BootstrapReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            Timber.e("context or intent is null")
            return
        }
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return
            val request = NetworkRequest.Builder().build()
            val networkCallback = NetworkCallback.getInstance(context)
            manager.requestNetwork(request, networkCallback)
        }
    }
}