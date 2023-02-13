package com.rouxinpai.demo.http

import com.google.gson.JsonObject
import com.rouxinpai.arms.model.bean.ApiResponse
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/13 10:53
 * desc   :
 */
interface Api {

    /**
     * 获取图片验证码
     */
    @GET("code")
    fun getCaptcha(): Flowable<ApiResponse<JsonObject>>

    /**
     * 获取更新信息
     */
    @GET("system/client/info?clientName=WORKPAD&clientType=android")
    fun getUpgradeInfo(): Flowable<ApiResponse<JsonObject>>
}