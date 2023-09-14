package com.rouxinpai.arms.print.model

import com.rouxinpai.arms.barcode.model.BarcodeInfoVO

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/5/15 14:55
 * desc   :
 */
data class PrintResultVO(val barcodeInfo: BarcodeInfoVO, var printSuccess: Boolean) {

    companion object {

        /**
         * 成功
         */
        fun success(barcodeInfo: BarcodeInfoVO) = PrintResultVO(barcodeInfo, true)

        /**
         * 失败
         */
        fun fail(barcodeInfo: BarcodeInfoVO) = PrintResultVO(barcodeInfo, false)
    }
}