package com.rouxinpai.arms.extension

import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/8/6 10:24
 * desc   :
 */

fun JsonElement.toRequestBody(): RequestBody {
    val mediaType = "application/json".toMediaType()
    return Gson().toJson(this).toRequestBody(mediaType)
}