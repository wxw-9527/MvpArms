package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/29 15:14
 * desc   :
 */
data class MaterialStockDetailDTO(
    val id: String,
    val barCode: String,
    val materialId: String,
    val materialCode: String,
    val storageQuantity: Float,
    val warehouseId: String?,
    val warehouseCode: String,
    val supplierCode: String?,
    val type: Int,
    val warehouseVO: WarehouseInfoDTO?,
)