package com.rouxinpai.arms.dict.api

import com.rouxinpai.arms.dict.model.CustomerDictItemDTO
import com.rouxinpai.arms.dict.model.DictItemDTO
import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.arms.model.bean.ApiResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/24 17:19
 * desc   :
 */
sealed interface DictApi {

    /**
     * 获取客户自定义字典数据
     */
    @POST
    fun listCustomerDictItems(
        @Url url: String = "${DomainUtils.getDomain()}cloud-master-data/customerDictData/selectDictDataList",
        @Body body: RequestBody,
    ): Observable<ApiResponse<List<CustomerDictItemDTO>>>

    /**
     * 获取系统字典数据
     */
    @POST
    fun listDictItems(
        @Url url: String = "${DomainUtils.getDomain()}system/dict/data/list",
        @Body body: RequestBody,
    ): Observable<ApiResponse<List<DictItemDTO>>>
}