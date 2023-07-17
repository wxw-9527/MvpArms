package com.rouxinpai.demo.feature.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.rouxinpai.arms.domain.BaseSplashActivity
import com.rouxinpai.demo.feature.domain.DomainConfigActivity
import com.rouxinpai.demo.feature.login.AccountLoginActivity
import com.rouxinpai.demo.feature.main.MainActivity

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 11:21
 * desc   :
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseSplashActivity() {

    companion object {

        /**
         * 启动[SplashActivity]页
         */
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, SplashActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun toDomainConfigPage() {
        DomainConfigActivity.start(this)
        finish()
    }

    override fun toLoginPage() {
        AccountLoginActivity.start(this)
        finish()
    }

    override fun toMainPage() {
        MainActivity.start(this)
        finish()
    }
}