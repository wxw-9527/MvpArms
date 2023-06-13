package com.rouxinpai.demo.model

import com.google.gson.annotations.SerializedName

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/28 10:31
 * desc   :
 */
data class AccessTokenDTO(@SerializedName("access_token") val accessToken: String)