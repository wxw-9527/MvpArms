package com.rouxinpai.arms.base.presenter

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.update.model.ClientNameEnum
import com.rouxinpai.arms.update.model.ClientTypeEnum
import io.reactivex.rxjava3.disposables.Disposable

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 14:52
 * desc   :
 */
interface IPresenter<V : IView> : DefaultLifecycleObserver {

    fun bind(lifecycle: Lifecycle, v: V)

    fun addDisposable(disposable: Disposable)

    /**
     * 缓存字典数据
     */
    fun listDictItems(showProgress: Boolean = false, onFail: ((e: Throwable) -> Unit)? = null)

    /**
     * 解析条码数据
     * @param barcode 条码
     */
    fun getBarcodeInfo(barcode: String)

    /**
     * 获取版本更新信息
     * @param clientType 客户端标志
     * @param clientName 项目名称
     * @param channel 渠道名
     */
    fun getUpdateInfo(clientType: ClientTypeEnum, clientName: ClientNameEnum, channel: String, onFail: ((e: Throwable) -> Unit)? = null)
}