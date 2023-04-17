package com.rouxinpai.arms.model

import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.model.bean.exception.EmptyException
import com.rouxinpai.arms.model.bean.exception.TokenTimeoutException
import com.shashank.sony.fancytoastlib.FancyToast
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/4/14 14:10
 * desc   :
 */

abstract class BaseSubscriber<T>(
    private val view: IView?,
    private val showErrorPage: Boolean = true
) :
    DisposableSubscriber<T>() {

    override fun onNext(t: T) {
        onData(t)
    }

    override fun onError(e: Throwable) {
        Timber.e(e)
        if (e is EmptyException) {
            onEmpty()
        } else {
            onFail(e)
        }
    }

    abstract fun onData(t: T)

    open fun onEmpty() {}

    open fun onFail(e: Throwable) {
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