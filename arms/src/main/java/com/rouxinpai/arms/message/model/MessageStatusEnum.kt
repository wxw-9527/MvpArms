package com.rouxinpai.arms.message.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/22 9:46
 * desc   :
 */
enum class MessageStatusEnum(val status: Int) {

    /**
     * 未读
     */
    UNREAD(0),

    /**
     * 已读
     */
    READ(1);

    companion object {

        /**
         *
         */
        fun fromStatus(status: Int): MessageStatusEnum {
            return entries.find { status == it.status } ?: UNREAD
        }
    }
}