package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/25 11:46
 * desc   :
 */
data class OutboundOrderDetailRecordDTO(
    val id: String,
    val outboundRecordId: String,
    val outboundOrderDetailId: String,
    val barCode: String,
    val materialCode: String,
    val outboundTime: String,
    val actualQuantity: Float,
    val warehouseVO: WarehouseInfoDTO?
)