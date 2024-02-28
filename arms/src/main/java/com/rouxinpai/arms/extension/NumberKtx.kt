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

/**
 * 将Float类型的数字转换为百分比形式的字符串。
 *
 * @param digits 小数点后保留的位数，默认为2。
 * @return 转换后的百分比字符串，保留指定位数的小数。
 */
fun Float.percentage(digits: Int = 2): String = formatPercentage(this.toDouble(), digits)

/**
 * 将Double类型的数字转换为百分比形式的字符串。
 *
 * @param digits 小数点后保留的位数，默认为2。
 * @return 转换后的百分比字符串，保留指定位数的小数。
 */
fun Double.percentage(digits: Int = 2): String = formatPercentage(this, digits)

/**
 * 将数字转换为百分比形式的字符串的共通实现。
 *
 * @param number 需要转换的数字，以Double类型表示。
 * @param digits 小数点后保留的位数，默认为2。
 * @return 转换后的百分比字符串，保留指定位数的小数。
 */
private fun formatPercentage(number: Double, digits: Int): String {
    return String.format("%.${digits}f%%", number * 100)
}