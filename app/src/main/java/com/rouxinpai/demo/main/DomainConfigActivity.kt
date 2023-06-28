package com.rouxinpai.demo.main

import android.content.Context
import android.content.Intent
import com.rouxinpai.arms.domain.BaseDomainConfigActivity
import com.rouxinpai.demo.main.login.AccountLoginActivity

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 15:22
 * desc   :
 */
class DomainConfigActivity : BaseDomainConfigActivity() {

    companion object {

        /**
         * 启动[DomainConfigActivity]页
         */
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, DomainConfigActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun toLoginPage() {
        AccountLoginActivity.start(this)
        finish()
    }
}