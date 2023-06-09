package com.rouxinpai.demo

import com.rouxinpai.arms.base.application.BaseApplication
import dagger.hilt.android.HiltAndroidApp

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/15 16:56
 * desc   :
 */
@HiltAndroidApp
class Application : BaseApplication() {

    override val loggable: Boolean
        get() = true

    override fun onCreate() {
        super.onCreate()
        initUmeng("64828b4ea1a164591b2ea15f", "Common")
    }

    override fun onTokenTimeout() {

    }
}