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

infix fun String.notBetween(that: String): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "notBetween")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.oneOf(that: String): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "in")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.notIn(that: String): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "notIn")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.like(that: String): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "like")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.notLike(that: String): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "notLike")
    jsonObject.addProperty("value", that)
    return jsonObject
}

fun String.isNull(): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "isNull")
    return jsonObject
}

fun String.isNotNull(): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "isNotNull")
    return jsonObject
}