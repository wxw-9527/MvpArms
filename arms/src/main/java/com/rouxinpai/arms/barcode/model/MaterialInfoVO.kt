package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/24 17:47
 * desc   :
 */
class MaterialInfoVO(
    val inboundOrderCode: String,
    val inboundOrderDetailId: String,
    val id: String,
    val code: String,
    val name: String,
    val spec: String,
    val unit: String,
    val batchCode: String?,
    val bomCode: String?,
    val quantity: Float,
    val stockQuantity: Float,
    val basicPackagingQuantity: Float,
)