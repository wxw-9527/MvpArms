package com.rouxinpai.arms.extension

import com.google.gson.JsonObject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/10 11:18
 * desc   :
 */

infix fun String.eq(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "eq")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.ne(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "ne")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.gt(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "gt")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.ge(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "ge")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.lt(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "lt")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.le(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "le")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.between(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "between")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.notBetween(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "notBetween")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.like(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "like")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.notLike(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "notLike")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.likeLeft(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "likeLeft")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.likeRight(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "likeRight")
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

infix fun String.oneOf(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "in")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.notIn(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "notIn")
    jsonObject.addProperty("value", that)
    return jsonObject
}

infix fun String.isAsc(that: Boolean): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("column", this)
    jsonObject.addProperty("isAsc", that)
    return jsonObject
}