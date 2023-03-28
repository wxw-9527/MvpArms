package com.rouxinpai.arms.update.api

import com.rouxinpai.arms.model.bean.ApiResponse
import com.rouxinpai.arms.update.model.UpdateInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/27 16:43
 * desc   :
 */
interface UpdateApi {

    /**
     * 获取更新信息
     */
    @GET("system/client/info")
    fun getUpdateInfo(@Query("clientType") clientType: String, @Query("clientName") clientName: String): Call<ApiResponse<UpdateInfo>>
}