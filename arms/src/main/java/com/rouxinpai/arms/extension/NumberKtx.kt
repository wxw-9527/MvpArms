package com.rouxinpai.arms.extension

import java.text.NumberFormat

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/1/9 11:06
 * desc   :
 */
fun Float.format(digits: Int = 3): String {
    return NumberFormat.getNumberInstance().apply {
        isGroupingUsed = false
        maximumFractionDigits = digits
    }.format(this)
}

/**
 * 修复Float相减计算精度丢失的问题
 */
infix fun Float?.subtract(f: Float?): Float {
    val bigDecimal1 = (this ?: 0f).toBigDecimal()
    val bigDecimal2 = (f ?: 0f).toBigDecimal()
    return bigDecimal1.subtract(bigDecimal2).toFloat()
}