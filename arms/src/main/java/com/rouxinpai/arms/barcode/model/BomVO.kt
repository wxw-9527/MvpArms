package com.rouxinpai.arms.barcode.model

import com.rouxinpai.arms.dict.util.DictUtil

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/14 13:50
 * desc   :
 */
data class BomVO(
    val bomId: String,
    val bomCode: String,
    val color: String,
) {

    companion object {

        /**
         *
         */
        fun fromDto(dto: BomDTO): BomVO {
            return BomVO(
                bomId = dto.bomId,
                bomCode = dto.bomCode,
                color = DictUtil.getInstance().convertMaterialColor(dto.color)?.value ?: dto.color,
            )
        }
    }
}