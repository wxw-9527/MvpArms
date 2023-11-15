package com.rouxinpai.arms.print.model

import androidx.annotation.StringRes
import com.rouxinpai.arms.R

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/14 17:38
 * desc   :
 */
enum class DirectionEnum(
    val value: Int,
    @StringRes val textResId: Int,
) {

    /**
     * 横向
     */
    HORIZONTAL(0, R.string.print_config__horizontal),

    /**
     * 纵向
     */
    VERTICAL(1, R.string.print_config__vertical);

    companion object {

        /**
         * 根据值获取枚举对象
         */
        fun fromValue(value: Int): DirectionEnum {
            return values().find { it.value == value } ?: HORIZONTAL
        }
    }
}