package com.rouxinpai.arms.domain

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 11:18
 * desc   :
 */
interface BaseSplashContract {

    interface View : IView {

        /**
         * 跳转到域名配置页面
         */
        fun toDomainConfigPage()

        /**
         * 跳转登录页
         */
        fun toLoginPage()

        /**
         * 跳转主页
         */
        fun toMainPage()
    }

    interface Presenter : IPresenter<View>
}