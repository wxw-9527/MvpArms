package com.rouxinpai.arms.update.api

import com.rouxinpai.arms.model.bean.ApiResponse
import com.rouxinpai.arms.update.model.UpdateInfo
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

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
    @GET
    fun getUpdateInfo(@Url url: String, @Query("clientType") clientType: String, @Query("clientName") clientName: String): Observable<ApiResponse<UpdateInfo>>
}