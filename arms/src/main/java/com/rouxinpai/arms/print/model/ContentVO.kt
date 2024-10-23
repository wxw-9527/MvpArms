package com.rouxinpai.arms.print.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/8/15 上午9:22
 * desc   :
 */
@Parcelize
data class ContentVO(
    val sourceKey: String?,
    val text: String?,
    val typeEnum: ContentTypeEnum,
): Parcelable {

    companion object {

        /**
         * 将[ContentDTO]转换成[ContentVO]
         */
        fun fromDto(dto: ContentDTO): ContentVO {
            return ContentVO(
                sourceKey = dto.sourceKey,
                text = dto.text,
                typeEnum = ContentTypeEnum.fromType(dto.type, dto.staticType)
            )
        }
    }
}