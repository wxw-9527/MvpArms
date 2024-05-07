package com.rouxinpai.arms.message.feature

import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.message.api.MessageApi
import com.rouxinpai.arms.message.model.MessageVO
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/22 15:17
 * desc   :
 */
class MessageDetailPresenter @Inject constructor() : BasePresenter<MessageDetailContract.View>(),
    MessageDetailContract.Presenter {

    override fun getMessage(id: String) {
        view?.showProgress()
        val disposable = retrofit.create<MessageApi>()
            .getMessage(messageId = id)
            .compose(responseTransformer())
            .map { dto -> MessageVO.fromDto(dto) }
            .compose(schedulersTransformer())
            .subscribeWith(object : DefaultObserver<MessageVO>(view) {

                override fun onData(t: MessageVO) {
                    super.onData(t)
                    view?.dismissProgress()
                    view?.showMessage(t)
                }
            })
        addDisposable(disposable)
    }
}