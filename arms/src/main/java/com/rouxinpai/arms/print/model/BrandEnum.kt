package com.rouxinpai.arms.print.model

import androidx.annotation.StringRes
import com.rouxinpai.arms.R

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/20 15:31
 * desc   : 打印机品牌
 */
enum class BrandEnum(val brand: String, @StringRes val brandNameResId: Int) {

    /**
     * 华辰联创
     */
    HCCTG("hcctg", R.string.print_config__hcctg),

    /**
     * 汉印
     */
    HPRT("hrpt", R.string.print_config__hrpt);

    companion object {

        /**
         *
         */
        fun fromBrand(brand: String?): BrandEnum {
            return entries.firstOrNull { it.brand == brand } ?: HCCTG
        }
    }
}