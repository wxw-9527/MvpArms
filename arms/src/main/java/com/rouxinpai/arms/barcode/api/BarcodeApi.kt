package com.rouxinpai.arms.barcode.api

import com.rouxinpai.arms.barcode.model.BarcodeInfoDTO
import com.rouxinpai.arms.model.bean.ApiResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

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
    @POST
    fun getBarcodeInfo(@Url url: String, @Body body: RequestBody): Observable<ApiResponse<BarcodeInfoDTO>>
}