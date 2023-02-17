package com.rouxinpai.arms.extension

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/17 9:34
 * desc   :
 */
val <T> T.type: Type
    get() = object : TypeToken<List<T>>() {}.type