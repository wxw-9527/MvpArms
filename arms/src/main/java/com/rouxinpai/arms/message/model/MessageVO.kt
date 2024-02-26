package com.rouxinpai.arms.message.model

import com.blankj.utilcode.util.TimeUtils

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/21 17:14
 * desc   :
 */
data class MessageVO(
    val id: String,
    val msgTitle: String,
    val msgContent: String,
    val msgType: Int,
    val messageStatusEnum: MessageStatusEnum,
    val friendlyTimeSpanByNow: String,
    val createTime: String,
    val createBy: String,
) {

    companion object {

        /**
         * 将[MessageDTO]转换成[MessageVO]
         */
        fun fromDto(dto: MessageDTO): MessageVO {
            return MessageVO(
                id = dto.id,
                msgTitle = dto.msgTitle,
                msgContent = dto.msgContent,
                msgType = dto.msgType,
                messageStatusEnum = MessageStatusEnum.fromStatus(dto.msgStatus),
                friendlyTimeSpanByNow = TimeUtils.getFriendlyTimeSpanByNow(
                    TimeUtils.string2Millis(dto.createTime)
                ),
                createTime = dto.createTime,
                createBy = dto.createBy
            )
        }
    }
}