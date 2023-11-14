package com.rouxinpai.demo.feature.domain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.rouxinpai.arms.domain.BaseDomainConfigActivity
import com.rouxinpai.arms.extension.format
import com.rouxinpai.arms.extension.subtract
import com.rouxinpai.demo.feature.login.AccountLoginActivity
import timber.log.Timber

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

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 测试format()方法
        val list = listOf(0.0f, 0f, 1.0f, 1f, 1.00f, 1.10f, 1.01f, 1.11f, 1.0110f, 1.011f)
        list.forEach {
            Timber.d("------> ${it.format()}")
        }
        // 测试subtract()方法
        Timber.d("------> ${1.0f - 0.9f}")
        Timber.d("------> ${1.0f subtract 0.9f}")
    }

    override fun toLoginPage() {
        AccountLoginActivity.start(this)
        finish()
    }
}