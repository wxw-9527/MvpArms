package com.rouxinpai.arms.model

import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.model.bean.exception.TokenTimeoutException
import io.reactivex.rxjava3.observers.DisposableObserver
import timber.log.Timber
import java.net.UnknownHostException

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/4/14 14:10
 * desc   :
 */

abstract class DefaultObserver<T : Any>(
    private val view: IView?,
    private val showErrorPage: Boolean = true
) : DisposableObserver<T>() {

    override fun onNext(t: T) = Unit

    override fun onComplete() = Unit

    override fun onError(e: Throwable) {
        Timber.e(e)
        view?.dismissProgress()
        val errorMsg = when (e) {
            is UnknownHostException -> "无法解析主机名称对应的IP地址，请检查网络"
            else -> e.message
        }
        view?.showErrorTip(errorMsg)
        if (e is TokenTimeoutException) {
            view?.tokenTimeout()
            return
        }
        if (showErrorPage) {
            view?.showErrorPage()
        }
    }
}