package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/27 10:26
 * desc   :
 */
data class BarcodeInfoDTO(
    val contextId: String,
    val batchCode: String,
    val barCode: String,
    val uniqueIdent: String,
    val barType: Int,
    val extendData: String?,
    val barContextDataList: List<BarContextDataDTO>?,
    val materialInfo: MaterialInfoDTO,
    val materialStockDetailVOList: List<MaterialStockDetailDTO>?,
    val warehouseInfo: WarehouseInfoDTO,
    val supplierVO: SupplierDTO?,
    val bomList: List<BomDTO>?,
    val outboundOrderDetailRecordVO: OutboundOrderDetailRecordDTO?,
    val inboundOrderDetailRecordVO: InboundOrderDetailRecordDTO?,
    val inboundOrderDetailVO: InboundOrderDetailDTO?,
)