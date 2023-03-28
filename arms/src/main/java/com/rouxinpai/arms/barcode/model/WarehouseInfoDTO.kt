package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/27 10:28
 * desc   :
 */
data class WarehouseInfoDTO(
    val id: String,
    val code: String,
    val name: String,
    val type: Int,
    val purpose: Int,
)