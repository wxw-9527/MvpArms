package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/24 16:19
 * desc   :
 */
data class BarcodeInfoVO(
    val barcode: String,
    val barTypeEnum: BarTypeEnum,
    val purchaseOrderNo: String,
    val inboundNo: String,
    val qcTaskNo: String,
) {

    companion object {

        /**
         *
         */
        fun convertFromDTO(dto: BarcodeInfoDTO): BarcodeInfoVO {
            var purchaseOrderNo = ""
            var inboundNo = ""
            var quality = ""
            var inboundOrderDetailId = ""
            var batchCode: String? = null
            var bomCode: String? = null
            var quantity = 0f
            var color = ""
            val snList = arrayListOf<String>()
            dto.barContextDataList?.forEach {
                when (it.billTypeCode) {
                    "purchaseOrderNo" -> purchaseOrderNo = it.billCode
                    "inboundNo" -> inboundNo = it.billCode
                    "quality" -> quality = it.billCode
                    "inboundOrderDetailId" -> inboundOrderDetailId = it.billCode
                    "batchCode" -> batchCode = it.billCode
                    "bomCode" -> bomCode = it.billCode
                    "quantity" -> quantity = it.billCode.toFloat()
                    "color" -> color = it.billCode
                    "sn" -> snList.add(it.billCode)
                }
            }
            return BarcodeInfoVO(
                barcode = dto.barCode,
                barTypeEnum = BarTypeEnum.getBarTypeEnum(dto.barType),
                purchaseOrderNo = purchaseOrderNo,
                inboundNo = inboundNo,
                qcTaskNo = quality,
            ).apply {
                when {
                    isMaterialBarcode -> {
                        material = MaterialInfoVO(
                            inboundOrderDetailId = inboundOrderDetailId,
                            id = dto.materialInfo.materialId,
                            code = dto.materialInfo.materialCode,
                            name = dto.materialInfo.materialName.orEmpty(),
                            spec = dto.materialInfo.materialSpec.orEmpty(),
                            unit = dto.materialInfo.materialUnit.orEmpty(),
                            color = color,
                            batchCode = batchCode,
                            bomCode = bomCode,
                            quantity = quantity,
                            stockQuantity = if (dto.itemVOList.isNullOrEmpty()) null else dto.itemVOList.map { it.storageQuantity }.sum(),
                            supplier = dto.supplierVO?.let { SupplierVO.fromDTO(it) },
                            snList = snList,
                            locationList = dto.itemVOList?.map { item ->
                                WarehouseInfoVO(
                                    id = item.warehouseId,
                                    code = item.warehouseCode,
                                    name = item.warehouseName.orEmpty(),
                                    purpose = 0,
                                    type = item.type
                                )
                            }.orEmpty()
                        )
                    }
                    isWarehouseLocationBarcode -> {
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
        }
    }

    /**
     * 物料条码
     */
    val isMaterialBarcode: Boolean
        get() = (BarTypeEnum.materialEnumList.contains(barTypeEnum))

    /**
     * 物料信息
     */
    lateinit var material: MaterialInfoVO

    /**
     * 库位码
     */
    val isWarehouseLocationBarcode: Boolean
        get() = (BarTypeEnum.warehouseEnumList.contains(barTypeEnum))

    /**
     * 库位信息
     */
    lateinit var warehouse: WarehouseInfoVO
}