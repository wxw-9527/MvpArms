package com.rouxinpai.arms.base.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import timber.log.Timber
import java.util.*

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/15 16:55
 * desc   :
 */
abstract class BaseApplication : Application(), IApplication {

    // 存放Activity实例的栈
    private val mActivities = Stack<Activity>()

    override val topActivity: Activity?
        get() = mActivities.lastOrNull()

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        initTimber()
    }

    override fun finishActivity(vararg cls: Class<*>) {
        if (cls.isEmpty()) {
            return
        }
        mActivities.forEach { activity ->
            cls.forEach { javaClass ->
                if (javaClass == activity.javaClass) {
                    finishActivity(activity)
                }
            }
        }
    }

    override fun finishToActivity(cls: Class<*>) {
        for (index in mActivities.lastIndex downTo 0) {
            val activity = mActivities[index]
            if (cls == activity.javaClass) {
                return
            }
            finishActivity(activity)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        addActivity(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        removeActivity(activity)
    }

    // 初始化日志打印框架
    private fun initTimber() {
        Timber.plant(object : Timber.DebugTree() {
            override fun isLoggable(tag: String?, priority: Int): Boolean = loggable
        })
    }

    // Activity入栈
    private fun addActivity(activity: Activity) {
        mActivities.add(activity)
    }

    // activity出栈
    private fun removeActivity(activity: Activity) {
        mActivities.remove(activity)
    }

    // 结束指定activity
    private fun finishActivity(activity: Activity) {
        activity.finish()
    }
}

