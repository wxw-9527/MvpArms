package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/27 10:28
 * desc   :
 */
data class MaterialInfoDTO(
    val id: String,
    val code: String,
    val name: String,
    val spec: String,
    val unit: String,
    val basicPackagingQuantity: Float,
)