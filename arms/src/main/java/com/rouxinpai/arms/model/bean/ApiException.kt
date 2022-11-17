package com.rouxinpai.arms.model.bean

import androidx.annotation.Keep

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 15:06
 * desc   :
 */
@Keep
open class ApiException(val code: Int, message: String) : RuntimeException(message)