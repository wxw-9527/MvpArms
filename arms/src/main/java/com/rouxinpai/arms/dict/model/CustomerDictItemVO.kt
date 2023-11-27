package com.rouxinpai.arms.dict.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/4/14 15:30
 * desc   :
 */
@Entity
data class CustomerDictItemVO(
    @Id var id: Long,
    val code: String,
    val key: String,
    val value: String,
    val status: Int,
) {

    companion object {

        /**
         *  将[CustomerDictItemDTO]转换为[CustomerDictItemVO]
         */
        fun fromDto(itemDTO: CustomerDictItemDTO): CustomerDictItemVO {
            return CustomerDictItemVO(
                id = 0L,
                code = itemDTO.code,
                key = itemDTO.dictDataKey,
                value = itemDTO.dictDataValue,
                status = itemDTO.status
            )
        }
    }
}