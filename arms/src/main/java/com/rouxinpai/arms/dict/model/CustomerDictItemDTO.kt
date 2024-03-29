package com.rouxinpai.arms.dict.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/4/3 12:03
 * desc   :
 */
data class CustomerDictItemDTO(
    val id: String,
    val code: String,
    val dictDataKey: String,
    val dictDataValue: String,
    val status: Int,
    val isAllowModification: Int,
    val type: Int,
)