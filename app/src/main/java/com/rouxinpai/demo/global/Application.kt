package com.rouxinpai.demo.global

import com.rouxinpai.arms.base.application.BaseApplication
import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.arms.message.util.MessageUtil
import com.rouxinpai.demo.feature.demo.notice.WebSocketService
import com.rouxinpai.demo.feature.splash.SplashActivity
import dagger.hilt.android.HiltAndroidApp

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/15 16:56
 * desc   :
 */
@HiltAndroidApp
class Application : BaseApplication() {

    companion object {

        /**
         * 应用唯一入口实例
         */
        lateinit var instance: Application
            private set
    }

    override val debug: Boolean
        get() = true

    override fun onCreate() {
        super.onCreate()
        instance = this
        // 初始化
        MessageUtil.init(WebSocketService::class.java)
    }

    override fun onTokenTimeout() {
        // 移除本地缓存的Token
        DomainUtils.removeAccessToken()
        // 跳转到登录页面
        val activity = topActivity
        if (activity != null) {
            SplashActivity.start(activity)
            finishAllActivities()
        }
    }
}