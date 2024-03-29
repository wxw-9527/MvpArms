package com.rouxinpai.arms.base.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.interfaces.ProgressViewInterface
import com.kongzue.dialogx.style.MaterialStyle
import com.kongzue.dialogx.util.views.NoArticulatedProgressView
import com.rouxinpai.arms.dict.util.DictUtil
import com.rouxinpai.arms.util.DownloadUtil
import com.rouxinpai.arms.util.HuaweiUtil
import com.tencent.mmkv.MMKV
import timber.log.Timber
import java.util.Stack

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/15 16:55
 * desc   :
 */
abstract class BaseApplication : Application(), IApplication {

    // 存放Activity实例的栈
    open val activities = Stack<Activity>()

    override val topActivity: Activity?
        get() = activities.lastOrNull()

    override fun onCreate() {
        super.onCreate()
        // 注册Activity生命周期回调
        registerActivityLifecycleCallbacks(this)
        // 初始化日志打印
        initTimber()
        // 初始化华为相关
        HuaweiUtil.initAGConnect(applicationContext)
        HuaweiUtil.initHwCrashHandler(debug)
        // 初始化MMKV
        initMmkv()
        // 初始化本地数据库
        initObjectBox()
        // 初始化DialogX
        initDialogX()
        // 初始化文件下载工具类
        initDownloadUtil()
    }

    override fun finishActivity(vararg cls: Class<*>) {
        if (cls.isEmpty()) {
            return
        }
        activities.forEach { activity ->
            cls.forEach { javaClass ->
                if (javaClass == activity.javaClass) {
                    finishActivity(activity)
                }
            }
        }
    }

    override fun finishToActivity(cls: Class<*>) {
        for (index in activities.lastIndex downTo 0) {
            val activity = activities[index]
            if (cls == activity.javaClass) {
                return
            }
            finishActivity(activity)
        }
    }

    override fun finishAllActivities() {
        activities.forEach { activity -> activity.finish() }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // 第一个Activity的onCreate中初始化Analytics SDK
        if (activities.isEmpty()) {
            HuaweiUtil.initAnalytics(debug, activity)
        }
        addActivity(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        removeActivity(activity)
    }

    // 初始化日志打印框架
    private fun initTimber() {
        Timber.plant(object : Timber.DebugTree() {
            override fun isLoggable(tag: String?, priority: Int): Boolean = debug
        })
    }

    // 初始化MMKV
    private fun initMmkv() {
        val rootDir = MMKV.initialize(this)
        Timber.d("mmkv root：$rootDir")
    }

    // 初始化本地数据库
    private fun initObjectBox() {
        DictUtil.getInstance().init(this, debug)
    }

    // 初始化DialogX
    private fun initDialogX() {
        // 初始化
        DialogX.init(this)
        // 去掉动画衔接效果以达到快速响应
        val materialStyle = object : MaterialStyle() {

            override fun overrideWaitTipRes(): WaitTipRes {

                return object : WaitTipRes() {

                    override fun overrideWaitView(context: Context, light: Boolean): ProgressViewInterface {
                        return NoArticulatedProgressView(context)
                    }
                }
            }
        }
        DialogX.globalStyle = materialStyle
    }

    // 初始化文件下载工具类
    private fun initDownloadUtil() {
        DownloadUtil.getInstance().init(this)
    }

    // Activity入栈
    private fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    // activity出栈
    private fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    // 结束指定activity
    private fun finishActivity(activity: Activity) {
        activity.finish()
    }
}