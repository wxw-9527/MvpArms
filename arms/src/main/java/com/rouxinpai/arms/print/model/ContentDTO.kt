package com.rouxinpai.arms.print.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/14 13:23
 * desc   :
 */
data class ContentDTO(
    val sourceKey: String,
    val font: String,
    val fontSize: Float,
    val isCenter: Boolean,
    val left: Float,
    val top: String,
    val type: String,
    val width: Float,
    val height: Float,
    val paramsTop: String,
    val text: String,
    val aligning: String,
)