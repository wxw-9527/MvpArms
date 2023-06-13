package com.rouxinpai.arms.domain

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 11:51
 * desc   :
 */
interface BaseDomainConfigContract {

    interface View: IView {
        fun toLoginPage()
    }

    interface Presenter: IPresenter<View> {
        fun getDomainConfiguration(invitationCode: String)
    }
}