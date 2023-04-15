package com.rouxinpai.arms.model.bean

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 15:07
 * desc   :
 */
class ApiResponse<out T>(val code: Int, val msg: String?, val total: Int, val data: T?) {

    companion object {

        // 请求成功标志
        private const val FLAG_REQUEST_SUCCESS = 200

        // 登录状态已过期
        private const val FLAG_TOKEN_TIMEOUT = 401
    }

    /**
     * 请求成功
     */
    val success: Boolean
        get() = FLAG_REQUEST_SUCCESS == code

    /**
     * token超时
     */
    val tokenTimeout: Boolean
        get() = FLAG_TOKEN_TIMEOUT == code


    /**
     *
     */
    val apiException: ApiException
        get() = if (tokenTimeout) TokenTimeoutException(code, errMsg) else ApiException(code, errMsg)

    /**
     *
     */
    private val errMsg: String
        get() = msg ?: "发生异常，errCode = $code"
}