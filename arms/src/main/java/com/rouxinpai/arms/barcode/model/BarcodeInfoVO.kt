package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/24 16:19
 * desc   :
 */
data class BarcodeInfoVO(val barcode: String, val barType: Int) {

    companion object {

        /**
         *
         */
        fun convertFromDTO(dto: BarcodeInfoDTO): BarcodeInfoVO {
            return BarcodeInfoVO(
                barcode = dto.barCode,
                barType = dto.barType
            ).apply {
                if (isMaterialBarcode) {
                    var inboundNo = ""
                    var inboundOrderDetailId = ""
                    var batchCode: String? = null
                    var bomVersionId: String? = null
                    var quantity = 0f
                    dto.barContextDataList?.forEach {
                        when (it.billTypeCode) {
                            "inboundNo" -> inboundNo = it.billCode
                            "inboundOrderDetailId" -> inboundOrderDetailId = it.billCode
                            "batchCode" -> batchCode = it.billCode
                            "bomVersionId" -> bomVersionId = it.billCode
                            "quantity" -> quantity = it.billCode.toFloat()
                        }
                    }
                    material = MaterialInfoVO(
                        inboundOrderCode = inboundNo,
                        inboundOrderDetailId = inboundOrderDetailId,
                        id = dto.materialInfo.id,
                        code = dto.materialInfo.code,
                        name = dto.materialInfo.name,
                        spec = dto.materialInfo.spec,
                        unit = dto.materialInfo.unit,
                        batchCode = batchCode,
                        bomVersionId = bomVersionId,
                        quantity = quantity,
                        basicPackagingQuantity = dto.materialInfo.basicPackagingQuantity
                    )
                } else if (isWarehouseLocationBarcode) {
                    warehouse = WarehouseInfoVO(
                        id = dto.warehouseInfo.id,
                        code = dto.warehouseInfo.code,
                        name = dto.warehouseInfo.name,
                        purpose = dto.warehouseInfo.purpose,
                        type = dto.warehouseInfo.type
                    )
                }
            }
        }

        // 条码类型
        private const val TYPE_MATERIAL = 1 // 物料
        private const val TYPE_WAREHOUSE_LOCATION = 14 // 库位
    }

    /**
     * 物料条码
     */
    val isMaterialBarcode: Boolean
        get() = TYPE_MATERIAL == barType

    /**
     * 物料信息
     */
    lateinit var material: MaterialInfoVO

    /**
     * 库位码
     */
    val isWarehouseLocationBarcode: Boolean
        get() = TYPE_WAREHOUSE_LOCATION == barType

    /**
     * 库位信息
     */
    lateinit var warehouse: WarehouseInfoVO
}