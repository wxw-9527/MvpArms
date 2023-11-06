package com.rouxinpai.demo.model.entity.demo.rv

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/7 21:11
 * desc   :
 */
data class MaterialDTO(
    val materialId: String,
    val materialCode: String,
    val materialName: String,
    val materialSpec: String?,
    val materialUnit: String?
)