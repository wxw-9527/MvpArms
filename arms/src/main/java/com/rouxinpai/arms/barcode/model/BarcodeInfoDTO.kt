package com.rouxinpai.arms.barcode.model

import com.google.gson.annotations.SerializedName

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
    @SerializedName("materialStockDetailVO") val materialStockDetail: MaterialStockDetailDTO?,
    val warehouseInfo: WarehouseInfoDTO,
    val supplierVo: SupplierDTO?
)