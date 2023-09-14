package com.rouxinpai.arms.print.api

import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.arms.model.bean.ApiResponse
import com.rouxinpai.arms.print.model.TemplateDTO
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/11 17:25
 * desc   :
 */
interface PrintApi {

    /**
     * 查询打印模板列表
     */
    @GET
    fun listTemplates(@Url url: String = "${DomainUtils.getDomain()}cloud-print/print-template/list"): Observable<ApiResponse<List<TemplateDTO>>>

    /**
     * 生成图片
     */
    @POST
    fun genImage(
        @Url url: String = "${DomainUtils.getDomain()}cloud-print/print/gen-image",
        @Body body: RequestBody
    ): Observable<ApiResponse<String>>

    /**
     * 打印
     */
    @POST
    fun print(
        @Url url: String = "${DomainUtils.getDomain()}cloud-print/print/code-job",
        @Body body: RequestBody
    ): Observable<ApiResponse<String>>
}