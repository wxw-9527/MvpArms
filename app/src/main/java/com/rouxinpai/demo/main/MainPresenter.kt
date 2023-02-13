package com.rouxinpai.demo.main

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.model.ErrorHandleSubscriber
import com.rouxinpai.arms.model.bean.ApiResponse
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.threadTransformer
import com.rouxinpai.demo.http.Api
import retrofit2.create
import timber.log.Timber
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 16:05
 * desc   :
 */
class MainPresenter @Inject constructor() : BasePresenter<MainContract.View>(),
    MainContract.Presenter {

    override fun print(text: String) {
        Timber.d("--------------> 开始请求")
        view?.showProgress()
        val flowable = retrofit.create<Api>().getCaptcha()
        val disposable = flowable
            .compose(threadTransformer())
            .compose(responseTransformer())
            .subscribeWith(object : ErrorHandleSubscriber<ApiResponse<JsonObject>>(view) {
                override fun onNext(t: ApiResponse<JsonObject>) {
                    super.onNext(t)
                    Timber.d("--------------> 请求成功！")
                    Timber.d(Gson().toJson(t))
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    Timber.d("--------------> 啊啊啊！发生异常了！！")
                }
            })
        addDisposable(disposable)
    }
}