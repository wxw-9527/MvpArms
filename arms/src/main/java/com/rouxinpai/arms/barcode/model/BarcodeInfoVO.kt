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
    val uniqueIdent: String,
    val barTypeEnum: BarTypeEnum,
    val snList: List<String>,
    val barContextDataMap: Map<String, String>,
    val bomVO: BomVO?,
) {

    companion object {

        /**
         *
         */
        fun convertFromDTO(dto: BarcodeInfoDTO): BarcodeInfoVO {
            val snList = dto.barContextDataList
                ?.filter { it.billTypeCode == BillTypeEnum.SN.billTypeCode }
                ?.map { it.billCode }
                ?.toMutableList() ?: mutableListOf()
            val map = dto.barContextDataList
                ?.filter { it.billTypeCode != BillTypeEnum.SN.billTypeCode }
                ?.associateBy({ it.billTypeCode }, { it.billCode })
                ?.toMutableMap() ?: mutableMapOf()
            return BarcodeInfoVO(
                contextId = dto.contextId,
                batchCode = dto.batchCode,
                barcode = dto.barCode,
                uniqueIdent = dto.uniqueIdent,
                barTypeEnum = BarTypeEnum.getBarTypeEnum(dto.barType),
                snList = snList,
                barContextDataMap = map,
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
                            unit = DictUtil.getInstance().convertMaterialUnit(unit)?.value,
                            supplier = dto.supplierVO?.let { SupplierVO.fromDTO(it) },
                            materialStockDetailVoList = dto.materialStockDetailVoList?.map { MaterialStockDetailVO.fromDto(it) }
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
        return barContextDataMap[enum.billTypeCode]
    }
}