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