package com.rouxinpai.arms.model

import com.rouxinpai.arms.model.bean.ApiResponse
import com.rouxinpai.arms.model.bean.PagingData
import com.rouxinpai.arms.model.bean.exception.EmptyException
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

fun <T : Any> schedulersTransformer() = FlowableTransformer<T, T> { upstream ->
    upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T : Any> responseTransformer() = FlowableTransformer<ApiResponse<T>, T> { upstream ->
    upstream.flatMap { response ->
        if (response.success) {
            val data = response.data
            if (data != null) {
                Flowable.just(data)
            } else {
                Flowable.error(EmptyException())
            }
        } else {
            Flowable.error(response.apiException)
        }
    }
}

fun <T : Collection<*>> pagingResponseTransformer() =
    FlowableTransformer<ApiResponse<T>, PagingData<T>> { upstream ->
        upstream.flatMap { response ->
            if (response.success) {
                val data = response.data
                if (data != null) {
                    Flowable.just(PagingData(response.total, data))
                } else {
                    Flowable.error(EmptyException())
                }
            } else {
                Flowable.error(response.apiException)
            }
        }
    }