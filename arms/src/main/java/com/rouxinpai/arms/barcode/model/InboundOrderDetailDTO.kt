package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/8/29 下午2:17
 * desc   :
 */
data class InboundOrderDetailDTO(
    val id: String,
    val inboundOrderId: String,
    val materialId: String,
    val materialCode: String,
    val batchCode: String,
    val furnaceNo: String?,
)