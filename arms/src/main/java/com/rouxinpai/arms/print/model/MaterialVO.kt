package com.rouxinpai.arms.print.model

import com.rouxinpai.arms.barcode.model.BarcodeInfoDTO

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/4/1 10:32
 * desc   :
 */
data class MaterialVO(
    val code: String,
    val name: String?,
    val supplerCode: String?,
    val supplerName: String?,
    val barcodeList: ArrayList<BarcodeVO>,
) {

    companion object {

        /**
         * 将[BarcodeInfoDTO]转换成[MaterialVO]
         */
        fun fromDto(dto: BarcodeInfoDTO): MaterialVO {
            val supplierShortName = dto.supplierVO?.shortName
            return MaterialVO(
                code = dto.materialInfo.materialCode,
                name = dto.materialInfo.materialName,
                supplerCode = dto.supplierVO?.supplierCode,
                supplerName = if (!supplierShortName.isNullOrEmpty()) supplierShortName else dto.supplierVO?.supplierName, // 供应商简称优先
                barcodeList = arrayListOf()
            )
        }
    }

    // 是否选中
    var isChecked: Boolean = false
}