package com.rouxinpai.demo.feature.login

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.extension.shake
import com.rouxinpai.demo.R
import com.rouxinpai.demo.databinding.AccountLoginActivityBinding
import com.rouxinpai.demo.feature.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/9 9:17
 * desc   :
 */
@AndroidEntryPoint
class AccountLoginActivity :
    BaseMvpActivity<AccountLoginActivityBinding, AccountLoginContract.View, AccountLoginPresenter>(),
    AccountLoginContract.View,
    View.OnClickListener {

    companion object {

        /**
         * 启动[AccountLoginActivity]页
         */
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, AccountLoginActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreateViewBinding(inflater: LayoutInflater): AccountLoginActivityBinding {
        return AccountLoginActivityBinding.inflate(inflater)
    }

    override fun hideNavButton(): Boolean {
        return true
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 绑定监听事件
        binding.ivCaptcha.setOnClickListener(this)
        binding.btnSignInNow.setOnClickListener(this)
        // 获取验证码
        binding.ivCaptcha.callOnClick()
    }

    override fun showAccountInfo(account: String?, password: String?, rememberMe: Boolean) {
        binding.etAccount.setText(account)
        binding.etPassword.setText(password)
        binding.cbRememberMe.isChecked = rememberMe
    }

    override fun showCaptcha(bitmap: Bitmap) {
        Glide.with(this).load(bitmap).into(binding.ivCaptcha)
    }

    override fun loginFailed() {
        binding.etCaptcha.text = null
    }

    override fun loginSuccessful() {
        MainActivity.start(this)
        finish()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_captcha -> onGetCaptchaClick()
            R.id.btn_sign_in_now -> onLoginClick()
        }
    }

    // 获取验证码
    private fun onGetCaptchaClick() {
        presenter.getCaptcha()
    }

    // 登录
    private fun onLoginClick() {
        // 账号
        val account = binding.etAccount.text?.toString()?.trim()
        if (account.isNullOrBlank()) {
            binding.etAccount.shake()
            showWarningTip(R.string.login__enter_account)
            return
        }
        // 密码
        val password = binding.etPassword.text?.toString()?.trim()
        if (password.isNullOrBlank()) {
            binding.etPassword.shake()
            showWarningTip(R.string.login__enter_password)
            return
        }
        // 验证码
        val captcha = binding.etCaptcha.text?.toString()?.trim()
        if (captcha.isNullOrBlank()) {
            binding.etCaptcha.shake()
            showWarningTip(R.string.login__enter_captcha)
            return
        }
        // 记住我
        val rememberMe = binding.cbRememberMe.isChecked
        // 登录
        presenter.login(account, password, captcha, rememberMe)
    }
}