package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/22 17:09
 * desc   :
 */
enum class BarTypeEnum(val barType: Int) {

    /**
     * 未知类型
     */
    UNKNOWN(-1),

    /**
     * 物料
     */
    MATERIAL(1),

    /**
     * 库位
     */
    WAREHOUSE(14);

    companion object {

        /**
         * 根据barType获取对应的枚举
         */
        fun getBarTypeEnum(barType: Int): BarTypeEnum {
            return enumValues<BarTypeEnum>().firstOrNull { it.barType == barType } ?: UNKNOWN
        }
    }
}