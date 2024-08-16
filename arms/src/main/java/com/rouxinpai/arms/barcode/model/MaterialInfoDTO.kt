package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/27 10:28
 * desc   :
 */
data class MaterialInfoDTO(
    val materialId: String,
    val materialCode: String,
    val materialName: String?,
    val materialSpec: String?,
    val materialUnit: String?,
    val basicPackagingQuantity: Float?,
    val materialCraftVersion: String?,
    val receiver: String?,
)