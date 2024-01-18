package com.rouxinpai.arms.dict.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/4/14 15:33
 * desc   :
 */
enum class CustomerDictEnum(val code: String) {

    /**
     * 物料单位
     */
    MATERIAL_UNIT("material_unit"), // 仓储、质检、看板

    /**
     * 物料颜色
     */
    MATERIAL_COLOR("material_colour"), // 仓储、质检、看板

    /**
     * 制作区分
     */
    PRODUCT_PARTITION("product_partition"), // 仓储

    /**
     * 判定方法
     */
    JUDGE_METHOD("judge_method"), // 质检
}