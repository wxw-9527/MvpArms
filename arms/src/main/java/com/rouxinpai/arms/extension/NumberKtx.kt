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
    return this.stripTrailingZeros().toPlainString().replace(Regex("\\.0*$"), "")
}

/**
 * 修复Float相减计算精度丢失的问题
 */
infix fun Float?.subtract(that: Float?): Float {
    return (this?.toBigDecimal() ?: BigDecimal.ZERO).subtract(that?.toBigDecimal() ?: BigDecimal.ZERO).toFloat()
}

/**
 * 修复Double相减计算精度丢失的问题
 */
infix fun Double?.subtract(that: Double?): Float {
    return (this?.toBigDecimal() ?: BigDecimal.ZERO).subtract(that?.toBigDecimal() ?: BigDecimal.ZERO).toFloat()
}