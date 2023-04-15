package com.rouxinpai.arms.model

import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.model.bean.TokenTimeoutException
import com.shashank.sony.fancytoastlib.FancyToast
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/4/14 14:10
 * desc   :
 */
abstract class DefaultSubscriber<T>(
    private val view: IView?,
    private val showErrorPage: Boolean = true
) : DisposableSubscriber<T>() {

    override fun onNext(t: T) {

    }

    override fun onError(e: Throwable) {
        Timber.e(e)
        view?.dismiss()
        view?.showToast(message = e.message, type = FancyToast.ERROR)
        if (e is TokenTimeoutException) {
            view?.tokenTimeout()
            return
        }
        if (showErrorPage) {
            view?.showErrorPage()
        }
    }

    override fun onComplete() {}
}