package com.rouxinpai.arms.print.model

import android.os.Parcelable
import com.blankj.utilcode.util.GsonUtils
import kotlinx.parcelize.Parcelize

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/11 17:40
 * desc   :
 */
@Parcelize
data class TemplateVO(
    val id: String,
    val name: String,
    val mediaSizeWidth: Float,
    val mediaSizeHeight: Float,
    val printWith: Float,
    val printHeight: Float,
    val offsetX: Int,
    val offsetY: Int,
    val sourceKeyList: List<String>
) : Parcelable {

    companion object {

        /**
         *
         */
        fun fromDto(dto: TemplateDTO): TemplateVO {
            val templateData = GsonUtils.fromJson(dto.data, TemplateDataDTO::class.java)
            val sourceKeyList = templateData.contents.map { it.sourceKey }
            return TemplateVO(
                id = dto.id,
                name = dto.name,
                mediaSizeWidth = dto.mediaSizeWidth,
                mediaSizeHeight = dto.mediaSizeHeight,
                printWith = (dto.mediaSizeWidth * 10) * 0.724f,
                printHeight = (dto.mediaSizeHeight * 10) * 0.724f,
                offsetX = ((864 - 8 * dto.mediaSizeWidth) / 2).toInt(),
                offsetY = ((864 - 8 * dto.mediaSizeHeight) / 2).toInt(),
                sourceKeyList = sourceKeyList
            )
        }
    }
}