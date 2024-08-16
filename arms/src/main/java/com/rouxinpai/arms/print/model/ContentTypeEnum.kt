package com.rouxinpai.arms.print.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/8/15 上午9:24
 * desc   :
 */
enum class ContentTypeEnum(private val type: String) {

    /**
     * 二维码
     */
    QRCODE("QRCODE"),

    /**
     * 文本
     */
    TEXT("TEXT"),

    /**
     * 静态文本
     */
    STATIC_TEXT("STATIC_TEXT");

    companion object {

        /**
         * 将[type]转换成[ContentTypeEnum]
         */
        fun fromType(type: String, staticType: String?): ContentTypeEnum {
            return when (type) {
                QRCODE.type -> QRCODE
                else -> {
                    if ("static_type" == staticType) {
                        STATIC_TEXT
                    } else {
                        TEXT
                    }
                }
            }
        }
    }
}