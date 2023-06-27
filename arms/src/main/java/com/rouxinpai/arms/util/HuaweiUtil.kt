package com.rouxinpai.arms.util

import android.content.Context
import com.huawei.agconnect.crash.AGConnectCrash
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.push.HmsMessaging
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/12 11:09
 * desc   :
 */
object HuaweiUtil {

    //
    private const val TAG = "HuaweiUtil"

    /**
     * 初始化Analytics Kit
     *
     * @param debug 是否开启debug模式，true代表开启，false代表关闭
     * @param context 上下文
     */
    internal fun initAnalytics(debug: Boolean, context: Context) {
        // 初始化Analytics Kit
        if (!debug) {
            HiAnalytics.getInstance(context)
        }
    }

    /**
     * 初始化华为崩溃信息收集服务
     */
    internal fun initHwCrashHandler(debug: Boolean) {
        // 如果debug为true，则开启崩溃信息收集服务，用于调试和测试
        AGConnectCrash.getInstance().enableCrashCollection(!debug)
    }

    /**
     * 设置用户ID
     */
    fun setUserId(userId: String?) {
        if (userId != null) {
            AGConnectCrash.getInstance().setUserId(userId)
        }
    }

    /**
     * 记录自定义事件
     */
    fun recordException(throwable: Throwable?) {
        if (throwable != null) {
            AGConnectCrash.getInstance().recordException(throwable)
        }
    }

    /**
     * 记录自定义事件
     */
    fun recordFatalException(throwable: Throwable?) {
        if (throwable != null) {
            AGConnectCrash.getInstance().recordFatalException(throwable)
        }
    }

    /**
     * 设置是否自动初始化。
     * 如果设置为true，SDK会自动生成AAID，自动申请Token，申请的Token通过onNewToken()回调方法返回。
     */
    fun setAutoInitEnabled(context: Context, isEnable: Boolean) {
        HmsMessaging.getInstance(context).isAutoInitEnabled = isEnable
    }

    /**
     * 获取Token。
     */
    fun getToken(context: Context, appId: String, tokenScope: String): Single<String> {
        return Single.create<String> { emitter ->
            val token = HmsInstanceId.getInstance(context).getToken(appId, tokenScope)
            if (token != null) {
                emitter.onSuccess(token)
            } else {
                emitter.onError(Throwable("获取Token失败"))
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 打开通知栏消息显示的开关。
     */
    fun turnOnPush(context: Context, failed: () -> Unit = {}, success: () -> Unit = {}) {
        HmsMessaging.getInstance(context).turnOnPush().addOnCompleteListener { task ->
            // 获取结果
            if (task.isSuccessful) {
                Timber.d(TAG, "turnOnPush successfully.")
                success.invoke()
            } else {
                Timber.d(TAG, "turnOnPush failed.")
                failed.invoke()
            }
        }
    }

    /**
     * 关闭通知栏消息显示的开关。
     */
    fun turnOffPush(context: Context, failed: () -> Unit = {}, success: () -> Unit) {
        HmsMessaging.getInstance(context).turnOffPush().addOnCompleteListener { task ->
            // 获取结果
            if (task.isSuccessful) {
                Timber.d(TAG, "turnOffPush successfully.")
                success.invoke()
            } else {
                Timber.d(TAG, "turnOffPush failed.")
                failed.invoke()
            }
        }
    }
}