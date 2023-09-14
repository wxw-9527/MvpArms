package com.rouxinpai.demo.global

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
    }

    override fun onTokenTimeout() {

    }

    override fun convertMaterialUnit(unit: String?): String {
        return "测试单位"
    }
}