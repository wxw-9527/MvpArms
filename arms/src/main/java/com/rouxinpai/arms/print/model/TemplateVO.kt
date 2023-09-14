package com.rouxinpai.arms.print.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/11 17:40
 * desc   :
 */
data class TemplateVO(
    val id: String,
    val name: String,
    val printWith: Float,
    val printHeight: Float,
    val offset: Int,
) {

    companion object {

        /**
         *
         */
        fun fromDto(dto: TemplateDTO): TemplateVO {
            return TemplateVO(
                id = dto.id,
                name = dto.name,
                printWith = (dto.mediaSizeWidth * 10) * 0.724f,
                printHeight = (dto.mediaSizeHeight * 10) * 0.724f,
                offset = if (100f == dto.mediaSizeWidth && 100f == dto.mediaSizeHeight) 32 else 224
            )
        }
    }
}