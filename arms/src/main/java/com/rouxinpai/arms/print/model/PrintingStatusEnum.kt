package com.rouxinpai.arms.print.model

import androidx.annotation.StringRes
import com.rouxinpai.arms.R

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/20 15:59
 * desc   :
 */
enum class PrintingStatusEnum(val status: Int, @StringRes val errorMsgResId: Int) {

    /**
     * 打印完成
     */
    COMPLETED(0, R.string.printing_status__completed),

    /**
     * 未知异常
     */
    UNKNOWN_ERROR(-1, R.string.printing_status__unknown_error),

    /**
     * 打印机正在打印中
     */
    PRINTING_IN_PROGRESS(-2, R.string.printing_status__in_progress),

    /**
     * 缺纸
     */
    PAPER_OUT(-3, R.string.printing_status__paper_out),

    /**
     * 纸舱盖开盖
     */
    COVER_OPEN(-4, R.string.printing_status__cover_open),

    /**
     * 与打印机通信失败
     */
    COMMUNICATION_FAILED(-5, R.string.printing_status__communication_failed),

    /**
     * 数据发送失败，通信异常
     */
    DATA_SEND_FAILED(-6, R.string.printing_status__data_send_failed),

    /**
     * 接收数据格式不正确
     */
    INVALID_DATA_FORMAT(-7, R.string.printing_status__invalid_data_format);

    companion object {

        fun fromStatus(status: Int?): PrintingStatusEnum {
            return values().find { status == it.status } ?: UNKNOWN_ERROR
        }
    }
}