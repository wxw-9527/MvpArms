package com.rouxinpai.arms.model

import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.model.bean.ApiException
import com.rouxinpai.arms.model.bean.ApiResponse
import com.rouxinpai.arms.model.bean.TokenTimeoutException
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.await
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 15:05
 * desc   :
 */
class Request<T> {

    /**
     * 是否执行默认的异常处理逻辑
     */
    private var mPerformDefaultErrorLogic: Boolean = true

    fun skipDefaultErrorLogic() {
        mPerformDefaultErrorLogic = false
    }

    /**
     * 是否展示异常页
     */
    private var mShowErrorPage: Boolean = true

    fun donotShowErrorPage() {
        mShowErrorPage = false
    }

    /**
     * 请求开始
     */
    private var mStart: (() -> Unit)? = null

    fun start(start: () -> Unit) {
        mStart = start
    }

    /**
     * 封装网络请求
     */
    private lateinit var mCall: suspend () -> Call<ApiResponse<T>>

    fun call(call: suspend () -> Call<ApiResponse<T>>) {
        mCall = call
    }

    /**
     * 请求成功
     */
    private lateinit var mSuccess: suspend ((total: Int, data: T) -> Unit)

    fun success(success: suspend (total: Int, data: T) -> Unit) {
        mSuccess = success
    }

    /**
     * 请求结束
     */
    private var mCompleted: (() -> Unit)? = null

    infix fun completed(completed: () -> Unit) {
        mCompleted = completed
    }

    /**
     * 请求失败
     */
    private var mFail: ((Exception) -> Unit)? = null

    fun fail(fail: (Exception) -> Unit) {
        mFail = fail
    }

    fun request(scope: CoroutineScope, view: IView?) {
        mStart?.invoke()
        scope.launch {
            try {
                val response = mCall.invoke().await()
                if (response.success) {
                    val data = response.data
                    if (data != null) {
                        mSuccess.invoke(response.total, data)
                    }
                    mCompleted?.invoke()
                } else if (response.tokenTimeout) {
                    throw TokenTimeoutException(response.code, response.msg)
                } else {
                    throw ApiException(response.code, response.msg)
                }
            } catch (e: CancellationException) {
                Timber.e(e)
            } catch (e: Exception) {
                Timber.e(e)
                if (mPerformDefaultErrorLogic) {
                    view?.dismiss()
                    view?.showToast(message = e.message, type = FancyToast.ERROR)
                    if (e is TokenTimeoutException) {
                        view?.tokenTimeout()
                        return@launch
                    }
                    if (mShowErrorPage) {
                        view?.showErrorPage()
                    }
                }
                mFail?.invoke(e)
            }
        }
    }
}