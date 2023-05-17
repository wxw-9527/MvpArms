package com.rouxinpai.arms.model

import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.model.bean.exception.EmptyException
import com.rouxinpai.arms.model.bean.exception.TokenTimeoutException
import com.shashank.sony.fancytoastlib.FancyToast
import io.reactivex.rxjava3.observers.DisposableObserver
import timber.log.Timber
import java.net.UnknownHostException

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/4/14 14:10
 * desc   :
 */

abstract class DefaultObserver<T>(
    private val view: IView?,
    private val showErrorPage: Boolean = true
) : DisposableObserver<T>() {

    override fun onNext(t: T) {
        onData(t)
    }

    override fun onError(e: Throwable) {
        if (e is EmptyException) {
            onEmpty()
        } else {
            Timber.e(e)
            onFail(e)
        }
    }

    open fun onData(t: T) {}

    open fun onEmpty() {}

    open fun onFail(e: Throwable) {
        view?.dismiss()
        val errorMsg = when (e) {
            is UnknownHostException -> "无法解析主机名称对应的IP地址，请检查网络"
            else -> e.message
        }
        view?.showToast(message = errorMsg, type = FancyToast.ERROR)
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