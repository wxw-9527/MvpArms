package com.rouxinpai.arms.message.api

import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.arms.extension.urlEncode
import com.rouxinpai.arms.message.model.MessageDTO
import com.rouxinpai.arms.model.bean.ApiResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Tag
import retrofit2.http.Url

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/21 15:04
 * desc   :
 */
interface MessageApi {

    /**
     * 获取未读消息数
     */
    @GET
    fun getUnreadMessagesCount(@Url url: String = "${DomainUtils.getDomain()}system/message/count"): Observable<ApiResponse<Int>>

    /**
     * 获取消息列表
     * 请求体示例：
     * {
     *   "pageNum": 1,
     *   "pageSize": 10,
     *   "orderBy": "createTime",
     *   "isAsc": false,
     *   "queryFields": [
     *     {
     *       "fieldName": "msgTitle",
     *       "operation": "like",
     *       "value": "123"
     *     },
     *     {
     *       "fieldName": "msgStatus",
     *       "operation": "in",
     *       "value": "0"
     *     }
     *   ]
     * }
     */
    @POST
    fun listMessages(
        @Url url: String = "${DomainUtils.getDomain()}system/message/list",
        @Body body: RequestBody,
    ): Observable<ApiResponse<List<MessageDTO>>>

    /**
     * 查看消息
     */
    @GET
    fun getMessage(
        @Tag messageId: String,
        @Url url: String = "${DomainUtils.getDomain()}system/message/${messageId.urlEncode()}",
    ): Observable<ApiResponse<MessageDTO>>

    /**
     * 更新消息状态
     * 请求体示例：{"ids":["1752163122089619458"],"status":0}
     * ids - 消息id列表
     * status -  0-标为未读，1-标为已读
     */
    @PUT
    fun updateStatus(
        @Url url: String = "${DomainUtils.getDomain()}system/message/change-status",
        @Body body: RequestBody,
    ): Observable<ApiResponse<String>>
}