package com.rouxinpai.arms.barcode.event

import org.greenrobot.eventbus.EventBus

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/28 10:26
 * desc   :
 */
data class BarcodeEvent(val barcode: String) {

    companion object {

        /**
         * 发送事件
         */
        fun post(barcode: String) {
            EventBus.getDefault().post(BarcodeEvent(barcode))
        }

        /**
         * 发送事件
         */
        fun postSticky(barcode: String) {
            EventBus.getDefault().postSticky(BarcodeEvent(barcode))
        }
    }

    /**
     * 移除事件
     */
    fun removeStickyEvent() {
        EventBus.getDefault().removeStickyEvent(this)
    }
}