package com.rouxinpai.arms.model

import com.rouxinpai.arms.model.bean.ApiResponse
import com.rouxinpai.arms.model.bean.PagingData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 15:05
 * desc   :
 */

fun <T : Any> schedulersTransformer() = ObservableTransformer<T, T> { upstream ->
    upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T : Any> responseTransformer() = ObservableTransformer<ApiResponse<T>, T> { upstream ->
    upstream.flatMap { response ->
        Observable.create {
            if (response.success) {
                val data = response.data
                if (data != null) {
                    it.onNext(data)
                }
                it.onComplete()
            } else {
                it.onError(response.apiException)
            }
        }
    }
}

fun <T : Collection<*>> pagingResponseTransformer() = ObservableTransformer<ApiResponse<T>, PagingData<T>> { upstream ->
        upstream.flatMap { response ->
            Observable.create {
                if (response.success) {
                    val data = response.data
                    if (data != null) {
                        it.onNext(PagingData(response.total, data))
                    }
                    it.onComplete()
                } else {
                    it.onError(response.apiException)
                }
            }
        }
    }