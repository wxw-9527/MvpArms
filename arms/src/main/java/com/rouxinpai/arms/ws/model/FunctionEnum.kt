package com.rouxinpai.arms.ws.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/30 11:15
 * desc   :
 */
enum class FunctionEnum(val functionName: String) {

    /**
     * 消息通知
     */
    NOTIFICATION(functionName = "/notification");

    companion object {

        /**
         * 根据名称获取枚举
         */
        fun fromName(functionName: String): FunctionEnum {
            return entries.first { functionName == it.functionName }
        }
    }
}