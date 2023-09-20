package com.rouxinpai.arms.print.model

import androidx.annotation.StringRes
import com.rouxinpai.arms.R

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/20 15:51
 * desc   :
 */
enum class PrinterStatusEnum(val status: Int?, @StringRes val errorMsgResId: Int) {

    /**
     * 未知异常
     */
    UNKNOWN_ERROR(null, R.string.printer_status__unknown_error),

    /**
     * 0 - 正常
     */
    NORMAL(0, R.string.printer_status__normal),

    /**
     * -1 - 通信异常
     */
    COMMUNICATION_ERROR(-1, R.string.printer_status__communication_error),

    /**
     * -2 - 缺纸
     */
    PAPER_OUT(-2, R.string.printer_status__paper_out),

    /**
     * -3 - 纸将尽
     */
    LOW_PAPER(-3, R.string.printer_status__low_paper),

    /**
     * -4 - 打印机开盖
     */
    COVER_OPEN(-4, R.string.printer_status__cover_open);

    companion object {

        /**
         *
         */
        fun fromStatus(status: Int?): PrinterStatusEnum {
            return values().find { status == it.status } ?: UNKNOWN_ERROR
        }
    }
}