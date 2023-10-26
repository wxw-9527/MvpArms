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
    return this.stripTrailingZeros().toPlainString()
}