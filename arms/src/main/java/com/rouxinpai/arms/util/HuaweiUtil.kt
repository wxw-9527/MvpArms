package com.rouxinpai.arms.util

import android.content.Context
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.crash.AGConnectCrash
import com.huawei.hms.analytics.HiAnalytics

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/12 11:09
 * desc   :
 */
object HuaweiUtil {

    /**
     * 初始化AGConnectInstance
     */
    internal fun initAGConnect(applicationContext: Context) {
        AGConnectInstance.initialize(applicationContext)
    }

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
}