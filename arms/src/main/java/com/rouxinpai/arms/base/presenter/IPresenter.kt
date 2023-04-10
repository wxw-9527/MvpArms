package com.rouxinpai.arms.base.presenter

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 14:52
 * desc   :
 */
interface IPresenter<V : IView> : DefaultLifecycleObserver {

    fun bind(lifecycle: Lifecycle, v: V)

    /**
     * 解析条码数据
     * @param barcode 条码
     */
    fun getBarcodeInfo(barcode: String)

    /**
     * 消费条码
     */
    fun consumeBarcode()

    /**
     * 获取版本更新信息
     * @param clientType 客户端标志
     * @param clientName 项目名称
     * @param channel 渠道名
     */
    fun getUpdateInfo(clientType: String = "android", clientName: String, channel: String)
}