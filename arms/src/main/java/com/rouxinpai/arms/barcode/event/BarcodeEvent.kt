package com.rouxinpai.arms.barcode.event

import org.greenrobot.eventbus.EventBus

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/28 10:26
 * desc   : 该类用于封装条码事件，并提供发送和移除事件的静态方法。
 *
 * @param barcode 条码字符串，是事件携带的数据。
 */
data class BarcodeEvent(val barcode: String) {

    companion object {

        /**
         * 发送条码事件到事件总线。
         *
         * @param barcode 要发送的条码字符串。
         */
        fun post(barcode: String) {
            EventBus.getDefault().post(BarcodeEvent(barcode))
        }

        /**
         * 以粘性事件的形式发送条码事件到事件总线。
         * 粘性事件会存储最近一次的事件信息，即使没有订阅者也会在注册后立即收到事件。
         *
         * @param barcode 要发送的粘性条码字符串。
         */
        fun postSticky(barcode: String) {
            EventBus.getDefault().postSticky(BarcodeEvent(barcode))
        }
    }

    /**
     * 从事件总线移除该粘性事件。
     *
     * @return 移除是否成功，成功返回true，反之为false。
     */
    fun removeStickyEvent(): Boolean {
        return EventBus.getDefault().removeStickyEvent(this)
    }
}