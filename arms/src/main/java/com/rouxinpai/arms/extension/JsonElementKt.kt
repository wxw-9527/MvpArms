package com.rouxinpai.arms.extension

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.reflect.Type

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/10 11:18
 * desc   :
 */


/**
 * 扩展属性，返回 List<T> 的 TypeToken。用于 Gson 序列化。
 */
val <T : Any> T.type: Type
    get() = object : TypeToken<List<T>>() {}.type

/**
 * 扩展属性，将 JsonElement 转换为 JsonObject，如果不是 JsonObject 则返回 null。
 */
val JsonElement.asJsonObjectOrNull: JsonObject?
    get() = if (isJsonObject) this.asJsonObject else null

/**
 * 扩展属性，将 JsonElement 转换为 JsonArray，如果不是 JsonArray 则返回 null。
 */
val JsonElement.asJsonArrayOrNull: JsonArray?
    get() = if (isJsonArray) asJsonArray else null

/**
 * 扩展属性，将 JsonElement 转换为 String，如果是 JsonNull 则返回 null。
 */
val JsonElement.asStringOrNull: String?
    get() = if (this.isJsonNull) null else asString

/**
 * 扩展属性，将 JsonElement 转换为 Int，如果是 JsonNull 则返回 null。
 */
val JsonElement.asIntOrNull: Int?
    get() = if (this.isJsonNull) null else asInt

/**
 * 扩展属性，将 JsonElement 转换为 Boolean，如果是 JsonNull 则返回 null。
 */
val JsonElement.asBooleanOrNull: Boolean?
    get() = if (this.isJsonNull) null else asBoolean

/**
 * 扩展属性，将 JsonElement 转换为 Double，如果是 JsonNull 则返回 null。
 */
val JsonElement.asDoubleOrNull: Double?
    get() = if (this.isJsonNull) null else asDouble

/**
 * 扩展属性，将 JsonElement 转换为 Float，如果是 JsonNull 则返回 null。
 */
val JsonElement.asFloatOrNull: Float?
    get() = if (this.isJsonNull) null else asFloat

/**
 * 扩展属性，将 JsonElement 转换为 Long，如果是 JsonNull 则返回 null。
 */
val JsonElement.asLongOrNull: Long?
    get() = if (this.isJsonNull) null else asLong

/**
 * 扩展属性，将 JsonElement 转换为 Byte，如果是 JsonNull 则返回 null。
 */
val JsonElement.asByteOrNull: Byte?
    get() = if (this.isJsonNull) null else asByte

/**
 * 扩展属性，将 JsonElement 转换为 Short，如果是 JsonNull 则返回 null。
 */
val JsonElement.asShortOrNull: Short?
    get() = if (this.isJsonNull) null else asShort

/**
 * 将 JsonElement 对象转换为 RequestBody 对象，使用 Gson 进行序列化。
 */
fun JsonElement.toRequestBody(): RequestBody {
    val mediaType = "application/json".toMediaType()
    return Gson().toJson(this).toRequestBody(mediaType)
}

/**
 * 中缀函数，构建表示相等操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.eq(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "eq")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示不相等操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.ne(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "ne")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示大于操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.gt(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "gt")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示大于等于操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.ge(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "ge")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示小于操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.lt(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "lt")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示小于等于操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.le(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "le")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示在范围内操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.between(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "between")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示不在范围内操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.notBetween(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "notBetween")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示模糊匹配操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.like(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "like")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示模糊匹配操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.orLike(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "orLike")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示不模糊匹配操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.notLike(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "notLike")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示左模糊匹配操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.likeLeft(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "likeLeft")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 中缀函数，构建表示右模糊匹配操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.likeRight(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "likeRight")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 扩展属性，构建表示为空操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
fun String.isNull(): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "isNull")
    return jsonObject
}

/**
 * 扩展属性，构建表示不为空操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
fun String.isNotNull(): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "isNotNull")
    return jsonObject
}

/**
 * 中缀函数，构建表示在列表内操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
infix fun String.oneOf(that: String?): JsonObject? {
    if (that.isNullOrEmpty()) return null
    val jsonObject = JsonObject()
    jsonObject.addProperty("fieldName", this)
    jsonObject.addProperty("operation", "in")
    jsonObject.addProperty("value", that)
    return jsonObject
}

/**
 * 扩展熟悉，构建表示不在列表内操作的 JsonObject。接收一个 String 作为左操作数（字段名）和一个可为空的 String 作为右操作数（值）。
 */
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