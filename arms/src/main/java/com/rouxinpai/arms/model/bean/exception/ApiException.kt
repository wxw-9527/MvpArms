package com.rouxinpai.arms.model.bean.exception

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 15:06
 * desc   :
 */
open class ApiException(val code: Int, message: String) : RuntimeException(message)