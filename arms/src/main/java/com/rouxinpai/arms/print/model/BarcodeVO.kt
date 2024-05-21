package com.rouxinpai.arms.print.model

import com.rouxinpai.arms.barcode.model.BarcodeInfoDTO

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/4/1 10:33
 * desc   :
 */
data class BarcodeVO(val barcode: String, val displayBarcode: String?) {

    companion object {

        /**
         *
         */
        fun fromDto(dto: BarcodeInfoDTO): BarcodeVO {
            return BarcodeVO(
                barcode = dto.barCode,
                displayBarcode = dto.extendData ?: dto.barCode
            )
        }
    }

    // 是否选中
    var isChecked: Boolean = false

    // 打印份数
    var copies: Float? = 1f
}