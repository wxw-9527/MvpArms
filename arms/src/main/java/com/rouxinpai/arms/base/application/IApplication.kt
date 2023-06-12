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

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
}