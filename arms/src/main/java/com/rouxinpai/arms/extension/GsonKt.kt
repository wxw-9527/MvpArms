package com.rouxinpai.arms.extension

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/17 9:34
 * desc   : Gson相关扩展方法
 */

/**
 *
 */
val <T : Any> T.type: Type
    get() = object : TypeToken<List<T>>() {}.type

/**
 *
 */
val JsonElement.asJsonObjectOrNull: JsonObject?
    get() = if (isJsonObject) this.asJsonObject else null

/**
 *
 */
val JsonElement.asJsonArrayOrNull: JsonArray?
    get() = if (isJsonArray) asJsonArray else null

/**
 *
 */
val JsonElement.asStringOrNull: String?
    get() = if (this.isJsonNull) null else asString

/**
 *
 */
val JsonElement.asIntOrNull: Int?
    get() = if (this.isJsonNull) null else asInt

/**
 *
 */
val JsonElement.asBooleanOrNull: Boolean?
    get() = if (this.isJsonNull) null else asBoolean

/**
 *
 */
val JsonElement.asDoubleOrNull: Double?
    get() = if (this.isJsonNull) null else asDouble

/**
 *
 */
val JsonElement.asFloatOrNull: Float?
    get() = if (this.isJsonNull) null else asFloat

/**
 *
 */
val JsonElement.asLongOrNull: Long?
    get() = if (this.isJsonNull) null else asLong

/**
 *
 */
val JsonElement.asByteOrNull: Byte?
    get() = if (this.isJsonNull) null else asByte

/**
 *
 */
val JsonElement.asShortOrNull: Short?
    get() = if (this.isJsonNull) null else asShort