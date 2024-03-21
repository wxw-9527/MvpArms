package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/14 17:42
 * desc   :
 */
data class MaterialStockDetailVO(
    val id: String,
    val barCode: String,
    val materialId: String,
    val materialCode: String,
    val warehouseId: String?,
    val warehouseCode: String,
    val warehouseName: String?,
    val storageQuantity: Float,
) {

    companion object {

        /**
         * 将[MaterialStockDetailDTO]转换成[MaterialStockDetailVO]
         */
        fun fromDto(dto: MaterialStockDetailDTO): MaterialStockDetailVO {
            return MaterialStockDetailVO(
                id = dto.id,
                barCode = dto.barCode,
                materialId = dto.materialId,
                materialCode = dto.materialCode,
                warehouseId = dto.warehouseId,
                warehouseCode = dto.warehouseCode,
                warehouseName = dto.warehouseName,
                storageQuantity = dto.storageQuantity,
            )
        }
    }
}