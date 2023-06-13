package com.rouxinpai.arms.domain.api

import com.rouxinpai.arms.domain.model.DomainConfigurationDTO
import com.rouxinpai.arms.model.bean.ApiResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 14:11
 * desc   :
 */
interface DomainApi {

    /**
     * 获取域名配置信息
     */
    @GET
    fun getDomainConfiguration(@Url url: String): Observable<ApiResponse<DomainConfigurationDTO>>
}