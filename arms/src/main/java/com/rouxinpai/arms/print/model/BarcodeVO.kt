package com.rouxinpai.arms.print.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/4/1 10:33
 * desc   :
 */
data class BarcodeVO(val barcode: String, val extendData: String?) {

    // 是否选中
    var isChecked: Boolean = false

    // 打印份数
    var copies: Float? = 1f
}