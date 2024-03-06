package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/22 17:09
 * desc   : 定义条码类型的枚举类。
 *
 * @param barType 条码类型的整数值。
 */
enum class BarTypeEnum(val barType: Int) {

    /**
     * 未知类型
     */
    UNKNOWN(-1),

    /**
     * 旧-物料编码
     */
    OLD_MATERIAL_SN(1), // TODO: 为了兼容新旧版本，下一版本删除

    /**
     * 旧-库位编码
     */
    OLD_WAREHOUSE_SN(14), // TODO: 为了兼容新旧版本，下一版本删除

    /**
     * 成品序列号
     */
    FINISHED_PRODUCT_SN(10),

    /**
     * 成品批次码
     */
    FINISHED_PRODUCT_BATCH(11),

    /**
     * 成品批次码子条码
     */
    FINISHED_PRODUCT_CHILD_BATCH(12),

    /**
     * 原材料序列号
     */
    RAW_MATERIAL_SN(20),

    /**
     * 原材料批次码
     */
    RAW_MATERIAL_BATCH(21),

    /**
     * 仓库序列号
     */
    WAREHOUSE_SN(30);

    companion object {

        /**
         * 根据条码类型的整数值获取对应的枚举实例。
         *
         * @param barType 条码类型的整数值。
         * @return 对应的枚举实例，如果没有找到匹配的类型，则返回UNKNOWN。
         */
        fun getBarTypeEnum(barType: Int): BarTypeEnum {
            return entries.find { it.barType == barType } ?: UNKNOWN
        }

        /**
         * 包含所有成品和原材料相关的条码类型的列表。
         */
        val materialEnumList = listOf(
            OLD_MATERIAL_SN,
            FINISHED_PRODUCT_SN,
            FINISHED_PRODUCT_BATCH,
            FINISHED_PRODUCT_CHILD_BATCH,
            RAW_MATERIAL_SN,
            RAW_MATERIAL_BATCH
        )

        /**
         * 包含所有仓库序列号相关的条码类型的列表。
         */
        val warehouseEnumList = listOf(OLD_WAREHOUSE_SN, WAREHOUSE_SN)
    }
}