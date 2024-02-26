package com.rouxinpai.arms.message.feature

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.message.model.MessageVO

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/21 16:43
 * desc   :
 */
sealed interface MessageListContract {

    interface View: IView {
        fun showMessageList(list: List<MessageVO>)
        fun showMoreMessageList(list: List<MessageVO>)
        fun refreshItemData(position: Int, messageVO: MessageVO)
    }

    interface Presenter: IPresenter<View> {
        fun listMessages(title: String?, status: Int?)
        fun listMoreMessages(title: String?, status: Int?)
        fun getMessage(position: Int, id: String)
        fun updateStatus(position: Int, id: String, status: Int)
    }
}