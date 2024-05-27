package com.rouxinpai.arms.barcode.model

import com.rouxinpai.arms.dict.util.DictUtil

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/24 16:19
 * desc   :
 */
data class BarcodeInfoVO(
    val contextId: String,
    val batchCode: String,
    val barcode: String,
    val displayBarcode: String,
    val uniqueIdent: String,
    val barTypeEnum: BarTypeEnum,
    val barContextDataList: List<Pair<String, String>>,
    val bomVO: BomVO?,
) {

    companion object {

        /**
         *
         */
        fun convertFromDTO(dto: BarcodeInfoDTO): BarcodeInfoVO {
            val list = dto.barContextDataList?.map { it.billTypeCode to it.billCode }.orEmpty()
            return BarcodeInfoVO(
                contextId = dto.contextId,
                batchCode = dto.batchCode,
                barcode = dto.barCode,
                displayBarcode = dto.extendData ?: dto.barCode,
                uniqueIdent = dto.uniqueIdent,
                barTypeEnum = BarTypeEnum.getBarTypeEnum(dto.barType),
                barContextDataList = list,
                bomVO = dto.bomList?.firstOrNull()?.let { BomVO.fromDto(it) },
            ).apply {
                when {
                    // 物料条码
                    isMaterialBarcode -> {
                        val unit = dto.materialInfo.materialUnit
                        material = MaterialInfoVO(
                            id = dto.materialInfo.materialId,
                            code = dto.materialInfo.materialCode,
                            name = dto.materialInfo.materialName.orEmpty(),
                            spec = dto.materialInfo.materialSpec.orEmpty(),
                            unit = DictUtil.getInstance().convertMaterialUnit(unit)?.value ?: unit,
                            supplier = dto.supplierVO?.let { SupplierVO.fromDTO(it) },
                            inboundOrderDetailRecordVo = dto.inboundOrderDetailRecordVO?.let { InboundOrderDetailRecordVO.fromDto(it) },
                            materialStockDetailVoList = dto.materialStockDetailVOList?.map { MaterialStockDetailVO.fromDto(it) },
                            outboundOrderDetailRecordVo = dto.outboundOrderDetailRecordVO?.let { OutboundOrderDetailRecordVO.fromDto(it) }
                        )
                    }
                    // 库位条码
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

    /**
     * 获取枚举对应的上下文数据
     */
    fun getBarContextData(enum: BillTypeEnum): String? {
        return barContextDataList.find { enum.billTypeCode == it.first }?.first
    }

    /**
     * 获取枚举对应的上下文数据
     */
    fun listBarContextData(enum: BillTypeEnum): List<String> {
        return barContextDataList.filter { enum.billTypeCode == it.first }.map { it.second }
    }

    /**
     * 获取枚举对应的上下文数据
     */
    fun getBarContextData(billTypeCode: String): String? {
        return barContextDataList.find { billTypeCode == it.first }?.first
    }

    /**
     * 获取枚举对应的上下文数据
     */
    fun listBarContextData(billTypeCode: String): List<String> {
        return barContextDataList.filter { billTypeCode == it.first }.map { it.second }
    }
}