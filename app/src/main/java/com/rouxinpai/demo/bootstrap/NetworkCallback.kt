package com.rouxinpai.demo.bootstrap

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/8 10:33
 * desc   :
 */
class NetworkCallback private constructor(private val context: Context) : ConnectivityManager.NetworkCallback() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: NetworkCallback? = null

        fun getInstance(context: Context): NetworkCallback {
            return instance ?: synchronized(this) {
                instance ?: NetworkCallback(context).also { instance = it }
            }
        }
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        try {
            val selfStartingRequest: WorkRequest = OneTimeWorkRequestBuilder<BootstrapWorker>().build()
            WorkManager.getInstance(context).enqueue(selfStartingRequest)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}