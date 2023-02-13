package com.rouxinpai.arms.model

import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.model.bean.TokenTimeoutException
import com.shashank.sony.fancytoastlib.FancyToast
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/13 11:12
 * desc   :
 */
abstract class ErrorHandleSubscriber<T>(private val view: IView?, private val showErrorPage: Boolean = true) : DisposableSubscriber<T>() {

    override fun onNext(t: T) {

    }

    override fun onError(t: Throwable) {
        Timber.e(t)
        view?.dismiss()
        view?.showToast(message = t.message, type = FancyToast.ERROR)
        if (t is TokenTimeoutException) {
            view?.tokenTimeout()
        }
        if (showErrorPage) {
            view?.showErrorPage()
        }
    }

    override fun onComplete() {

    }
}