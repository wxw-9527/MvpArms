package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/25 15:12
 * desc   :
 */
data class InboundOrderDetailRecordDTO(
    val id: String,
    val inboundOrderRecordId: String,
    val inboundOrderDetailId: String,
    val barCode: String,
    val actualQuantity: Float,
)