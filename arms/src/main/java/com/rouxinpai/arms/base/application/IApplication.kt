package com.rouxinpai.arms.base.application

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/16 16:31
 * desc   :
 */
interface IApplication : Application.ActivityLifecycleCallbacks {

    /**
     * 栈顶Activity实例
     */
    val topActivity: Activity?

    /**
     * 是否为调试模式
     */
    val debug: Boolean

    /**
     * 闪屏页等待时长
     * 默认为1秒
     */
    val splashWaitDuration: Long
        get() = 1000L

    /**
     * 请求超时时长
     * 默认为10秒
     */
    val requestTimeout: Long
        get() = 10L

    /**
     * 心跳间隔
     * 默认为10秒
     */
    val heartbeatInterval: Long
        get() = 10L

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(vararg cls: Class<*>)

    /**
     * 结束至指定类名的Activity
     */
    fun finishToActivity(cls: Class<*>)

    /**
     * 结束全部Activity
     */
    fun finishAllActivities()

    /**
     * 登录超时
     */
    fun onTokenTimeout()

    /**
     * 转换物料单位
     */
    fun convertMaterialUnit(unit: String?): String? = unit

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
}