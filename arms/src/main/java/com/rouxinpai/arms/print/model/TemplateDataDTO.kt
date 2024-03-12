package com.rouxinpai.arms.print.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/14 13:21
 * desc   :
 */
data class TemplateDataDTO(
    val width: Float,
    val height: Float,
    val x: Float,
    val y: Float,
    val contents: List<ContentDTO>,
)