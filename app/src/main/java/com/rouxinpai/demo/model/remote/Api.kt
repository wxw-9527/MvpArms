package com.rouxinpai.demo.model.remote

import com.rouxinpai.arms.model.bean.ApiResponse
import com.rouxinpai.demo.model.entity.login.AccessTokenDTO
import com.rouxinpai.demo.model.entity.login.CaptchaDTO
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

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
    @GET
    fun getCaptcha(@Url url: String): Observable<ApiResponse<CaptchaDTO>>

    /**
     * 账号登录
     */
    @POST
    fun accountLogin(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Observable<ApiResponse<AccessTokenDTO>>
}