package com.rouxinpai.demo.http

import com.google.gson.JsonObject
import com.rouxinpai.arms.model.bean.ApiResponse
import com.rouxinpai.arms.update.model.UpdateInfo
import com.rouxinpai.demo.model.ArrivalOrderDTO
import com.rouxinpai.demo.model.CustomerDictDataDTO
import com.rouxinpai.demo.model.Detail
import com.rouxinpai.demo.model.ProductionDTO
import io.reactivex.rxjava3.core.Flowable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
    @GET("system/client/info?clientName=test1&clientType=android")
    fun getUpgradeInfo(): Flowable<ApiResponse<UpdateInfo>>

    /**
     * 获取客户自定义字典数据
     */
    @POST("system/customerDictData/selectDictDataList")
    fun listCustomerDictData(@Body body: RequestBody): Flowable<ApiResponse<List<CustomerDictDataDTO>>>

    /**
     * 获取今日组装数量
     */
    @GET("cloud-work-pad/production/output")
    fun getAssemblyQuantityToday(): Flowable<ApiResponse<JsonObject>>

    /**
     * 获取任务单列表
     */
    @GET("cloud-work-pad/production/list")
    fun listProductions(): Flowable<ApiResponse<List<ProductionDTO>>>

    @GET("wms-v12/inboundOrder/getInboundOrderAndOrderDetailList?inboundOrderId=1645337204137713666")
    fun getInboundOrderAndOrderDetailList(): Flowable<ApiResponse<Detail>>

    /**
     * 查询到货单列表
     *
     */
    @POST("wms-v12/inboundOrder/selectInboundOrderListByPage")
    fun listArrivalOrders(@Body body: RequestBody): Flowable<ApiResponse<List<ArrivalOrderDTO>>>
}