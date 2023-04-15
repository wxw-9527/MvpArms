package com.rouxinpai.arms.base.activity

import android.content.IntentFilter
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.module.BaseLoadMoreModule
import com.rouxinpai.arms.annotation.BarcodeScanningReceiverEnabled
import com.rouxinpai.arms.barcode.event.BarcodeEvent
import com.rouxinpai.arms.barcode.receiver.BarcodeScanningReceiver
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.update.model.UpdateInfo
import constant.UiType
import model.UiConfig
import model.UpdateConfig
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import update.UpdateAppUtils
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 15:48
 * desc   :
 */
abstract class BaseMvpActivity<VB : ViewBinding, V : IView, P : IPresenter<V>> :
    BaseActivity<VB>() {

    @Inject
    lateinit var presenter: P

    open val mLoadMoreModule: BaseLoadMoreModule? = null

    // 是否使用条码解析
    private var mBarcodeScanningReceiverEnabled: Boolean = false

    // 扫描结果解析广播
    private lateinit var mReceiver: BarcodeScanningReceiver

    override fun onInit(savedInstanceState: Bundle?) {
        @Suppress("UNCHECKED_CAST")
        presenter.bind(lifecycle, this as V)
        super.onInit(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        // 注册广播
        mBarcodeScanningReceiverEnabled = javaClass.isAnnotationPresent(BarcodeScanningReceiverEnabled::class.java)
        if (mBarcodeScanningReceiverEnabled) {
            val intentFilter = IntentFilter().apply {
                BarcodeScanningReceiver.sActions.forEach { addAction(it) }
            }
            mReceiver = BarcodeScanningReceiver()
            registerReceiver(mReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        // 取消注册广播
        if (mBarcodeScanningReceiverEnabled) {
            unregisterReceiver(mReceiver)
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    open fun onBarcodeEvent(event: BarcodeEvent) {
        presenter.getBarcodeInfo(event.barcode)
    }

    override fun loadMoreComplete() {
        super.loadMoreComplete()
        mLoadMoreModule?.loadMoreComplete()
    }

    override fun loadMoreEnd(gone: Boolean) {
        super.loadMoreEnd(gone)
        mLoadMoreModule?.loadMoreEnd(gone)
    }

    override fun loadMoreFail() {
        super.loadMoreFail()
        mLoadMoreModule?.loadMoreFail()
    }

    override fun showUpdateInfo(updateInfo: UpdateInfo) {
        super.showUpdateInfo(updateInfo)
        // ui配置
        val uiConfig = UiConfig().apply {
            uiType = UiType.PLENTIFUL
        }
        // 更新配置
        val updateConfig = UpdateConfig().apply {
            force = updateInfo.isForceUpgrade
            isShowNotification = false
            alwaysShowDownLoadDialog = true
        }
        // 检查更新
        UpdateAppUtils
            .getInstance()
            .apkUrl(updateInfo.apkFileUrl)
            .updateTitle("新版本")
            .updateContent(updateInfo.clientLog)
            .uiConfig(uiConfig)
            .updateConfig(updateConfig)
            .update()
    }
}