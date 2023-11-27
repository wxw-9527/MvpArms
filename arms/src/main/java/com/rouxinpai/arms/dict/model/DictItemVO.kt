package com.rouxinpai.arms.dict.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/26 11:21
 * desc   :
 */
@Entity
data class DictItemVO(
    @Id var id: Long,
    val dictCode: String,
    val dictLabel: String,
    val dictValue: String,
    val dictType: String,
    val dictSort: Int,
) {

    companion object {

        /**
         * 将[DictItemDTO]转换成[DictItemVO]
         */
        fun fromDto(dto: DictItemDTO): DictItemVO {
            return DictItemVO(
                id = 0L,
                dictCode = dto.dictCode,
                dictLabel = dto.dictLabel,
                dictValue = dto.dictValue,
                dictType = dto.dictType,
                dictSort = dto.dictSort
            )
        }
    }
}