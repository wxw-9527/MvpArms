package com.rouxinpai.arms.extension

import com.blankj.utilcode.util.TimeUtils
import com.rouxinpai.arms.constant.TimeConstants
import com.rouxinpai.calendarview.Calendar

/**
 * 给日历对象增加开始时间后缀
 */
fun Calendar.addStartSuffix(): String {
    return "${TimeUtils.millis2String(this.timeInMillis, TimeConstants.Y_MO_D)} 00:00:00"
}

/**
 * 给日历对象增加结束时间后缀
 */
fun Calendar.addEndSuffix(): String {
    return "${TimeUtils.millis2String(this.timeInMillis, TimeConstants.Y_MO_D)} 23:59:59"
}
