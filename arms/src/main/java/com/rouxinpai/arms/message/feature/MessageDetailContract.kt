package com.rouxinpai.arms.message.feature

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.message.model.MessageVO

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/22 15:16
 * desc   :
 */
sealed interface MessageDetailContract {

    interface View : IView {
        fun showMessage(messageVO: MessageVO)
    }

    interface Presenter : IPresenter<View> {
        fun getMessage(id: String)
    }
}