package com.rouxinpai.arms.model

import com.rouxinpai.arms.model.bean.ApiException
import com.rouxinpai.arms.model.bean.ApiResponse
import com.rouxinpai.arms.model.bean.TokenTimeoutException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 15:05
 * desc   :
 */

/**
 * 线程切换处理
 */
fun <T : Any> threadTransformer() = FlowableTransformer<T, T> { upstream ->
    upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

/**
 * 处理返回结果
 */
fun <T> responseTransformer() = FlowableTransformer<ApiResponse<T>, ApiResponse<T>> { upstream ->
    upstream.flatMap { response ->
        if (response.success) {
            Flowable.just(response)
        } else {
            val apiException = if (response.tokenTimeout) {
                TokenTimeoutException(response.code, response.msg)
            } else {
                ApiException(response.code, response.msg)
            }
            Flowable.error(apiException)
        }
    }
}