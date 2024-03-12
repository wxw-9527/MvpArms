package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/14 13:49
 * desc   :
 */
data class BomDTO(
    val bomId: String,
    val bomCode: String,
    val masterMaterialCode: String,
    val color: String,
    val rootVersion: String,
    val masterVersion: String,
    val state: Int,
    val deptId: String,
    val sopCode: String,
)