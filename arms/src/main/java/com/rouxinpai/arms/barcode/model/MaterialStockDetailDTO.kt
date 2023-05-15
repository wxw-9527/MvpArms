package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/29 15:14
 * desc   :
 */
data class MaterialStockDetailDTO(
    val barCode: String,
    val materialId: String,
    val materialCode: String,
    val storageQuantity: Float,
    val warehouseId: String,
    val warehouseCode: String,
    val warehouseName: String?,
)