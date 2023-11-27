package com.rouxinpai.arms.dict.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/26 11:21
 * desc   :
 */
data class DictItemDTO(
    val dictCode: String,
    val dictLabel: String,
    val dictValue: String,
    val dictType: String,
    val dictSort: Int,
    val cssClass: String?
)