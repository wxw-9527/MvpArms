package com.rouxinpai.arms.model.bean


/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 15:17
 * desc   :
 */
class TokenTimeoutException(code: Int, message: String) : ApiException(code, message)