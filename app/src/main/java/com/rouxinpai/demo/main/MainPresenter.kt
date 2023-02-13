package com.rouxinpai.demo.main

import com.google.gson.JsonObject
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.model.request
import com.rouxinpai.demo.http.Api
import kotlinx.coroutines.delay
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
        request<JsonObject> {
            call {
                retrofit.create<Api>().getCaptcha()
            }
            success { total, data ->
                Timber.d("请求成功")
                delay(2000L)
                Timber.d("延时成功")
            }
        }
    }
}