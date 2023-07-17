package com.rouxinpai.demo.feature.login

import android.graphics.Bitmap
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/9 9:17
 * desc   :
 */
interface AccountLoginContract {

    interface View: IView {
        fun showAccountInfo(account: String?, password: String?, rememberMe: Boolean)
        fun showCaptcha(bitmap: Bitmap)
        fun loginFailed()
        fun loginSuccessful()
    }

    interface Presenter: IPresenter<View> {
        fun getCaptcha()
        fun login(account: String, password: String, captcha: String, rememberMe: Boolean)
    }
}