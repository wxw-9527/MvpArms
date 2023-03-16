package com.rouxinpai.arms.extension

import com.google.gson.JsonObject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/10 11:18
 * desc   :
 */

infix fun String.eq(that: String): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "eq")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.between(that: String): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "between")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.`in`(that: String): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "in")
    jsonObject.addProperty("value", that)
    return jsonObject
}