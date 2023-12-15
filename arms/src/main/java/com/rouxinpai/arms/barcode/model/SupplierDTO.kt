package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/3 15:53
 * desc   :
 */
data class SupplierDTO(
    val supplierId: String,
    val supplierCode: String,
    val supplierName: String,
    val shortName: String?,
    val contact: String?,
    val contactTel: String?,
    val contactAddress: String?,
    val supplierStatus: Int,
    val remark: String?,
)