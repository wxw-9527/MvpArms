package com.rouxinpai.arms.barcode.api

import com.rouxinpai.arms.barcode.model.BarcodeInfoDTO
import com.rouxinpai.arms.model.bean.ApiResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/28 10:15
 * desc   :
 */
interface BarcodeApi {

    /**
     * 获取条码内容
     */
    @POST("ident/bill-info/query")
    fun getBarcodeInfo(@Body body: RequestBody): Call<ApiResponse<BarcodeInfoDTO>>
}