package com.rouxinpai.arms.print.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/3 11:33
 * desc   : 打印模板
 */
data class TemplateDTO(
    val id: String,
    val name: String,
    val mediaSizeWidth: Float,
    val mediaSizeHeight: Float,
)