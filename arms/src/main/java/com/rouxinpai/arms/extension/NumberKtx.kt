package com.rouxinpai.arms.extension

import java.math.BigDecimal

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/1/9 11:06
 * desc   :
 */
fun Float.format(): String {
    return this.toBigDecimal().format()
}

fun Double.format(): String {
    return this.toBigDecimal().format()
}

fun BigDecimal.format(): String {
    val str = this.stripTrailingZeros().toPlainString()
    // 如果以.0结尾，去掉.0（修复java的bug，在java1.8中已修复）
    return if (str.endsWith(".0")) {
        str.substring(0, str.length - 2)
    } else {
        str
    }
}

/**
 * 修复Float相减计算精度丢失的问题
 */
infix fun Float?.subtract(f: Float?): Float {
    val bigDecimal1 = (this ?: 0f).toBigDecimal()
    val bigDecimal2 = (f ?: 0f).toBigDecimal()
    return bigDecimal1.subtract(bigDecimal2).toFloat()
}