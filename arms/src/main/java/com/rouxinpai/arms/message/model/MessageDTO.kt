package com.rouxinpai.arms.message.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/21 15:44
 * desc   :
 */
data class MessageDTO(
    val id: String,
    val msgTitle: String,
    val msgContent: String,
    val msgStatus: Int,
    val msgType: Int,
    val createTime: String,
    val createBy: String,
)
