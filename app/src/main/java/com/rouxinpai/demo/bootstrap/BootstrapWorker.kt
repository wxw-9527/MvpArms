package com.rouxinpai.demo.bootstrap

import android.content.Context
import android.net.ConnectivityManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rouxinpai.arms.message.util.MessageUtil
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/8 10:34
 * desc   :
 */
class BootstrapWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        try {
            // 启动后台服务
            val context = applicationContext
            MessageUtil.startService(context)
            // 取消网络状态监听
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            manager?.unregisterNetworkCallback(NetworkCallback.getInstance(context))
        } catch (e: Exception) {
            Timber.e(e)
            return Result.retry()
        }
        return Result.success()
    }
}