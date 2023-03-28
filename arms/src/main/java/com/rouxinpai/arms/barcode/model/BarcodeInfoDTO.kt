package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/27 10:26
 * desc   :
 */
data class BarcodeInfoDTO(
    val barCode: String,
    val uniqueIdent: String,
    val barType: Int,
    val barContextDataList: List<BarContextDataDTO>?,
    val materialInfo: MaterialInfoDTO,
    val warehouseInfo: WarehouseInfoDTO,
)