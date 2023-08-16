@file:Suppress("UNCHECKED_CAST")

package com.rouxinpai.arms.base.activity

import android.content.IntentFilter
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.module.BaseLoadMoreModule
import com.kongzue.kongzueupdatesdk.v3.UpdateUtil
import com.rouxinpai.arms.R
import com.rouxinpai.arms.annotation.BarcodeScanningReceiverEnabled
import com.rouxinpai.arms.barcode.event.BarcodeEvent
import com.rouxinpai.arms.barcode.receiver.BarcodeScanningReceiver
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.ILoadMore
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.base.view.LoadMoreDelegate
import com.rouxinpai.arms.update.model.UpdateInfo
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 15:48
 * desc   :
 */
abstract class BaseMvpActivity<VB : ViewBinding, V : IView, P : IPresenter<V>> :
    BaseActivity<VB>(), ILoadMore {

    @Inject
    lateinit var presenter: P

    // 是否使用条码解析
    private var mBarcodeScanningReceiverEnabled: Boolean = false

    // 版本更新实例
    private var mUpdateUtil: UpdateUtil? = null

    // 扫描结果解析广播
    private lateinit var mReceiver: BarcodeScanningReceiver

    // 加载更多代理类实例
    private lateinit var mLoadMoreDelegate: LoadMoreDelegate

    open val mLoadMoreModule: BaseLoadMoreModule? = null

    override fun onInit(savedInstanceState: Bundle?) {
        presenter.bind(lifecycle, this as V)
        super.onInit(savedInstanceState)
        mLoadMoreDelegate = LoadMoreDelegate(mLoadMoreModule)
    }

    override fun onResume() {
        super.onResume()
        // 注册广播
        mBarcodeScanningReceiverEnabled = javaClass.isAnnotationPresent(BarcodeScanningReceiverEnabled::class.java)
        if (mBarcodeScanningReceiverEnabled) {
            val intentFilter = IntentFilter().apply {
                BarcodeScanningReceiver.sActions.forEach { action -> addAction(action) }
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

    override fun onDestroy() {
        super.onDestroy()
        // 终止更新流程，释放内存
        mUpdateUtil?.recycle()
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    open fun onBarcodeEvent(event: BarcodeEvent) {
        presenter.getBarcodeInfo(event.barcode)
    }

    override fun loadMoreComplete() {
        mLoadMoreDelegate.loadMoreComplete()
    }

    override fun loadMoreEnd(gone: Boolean) {
        mLoadMoreDelegate.loadMoreEnd(gone)
    }

    override fun loadMoreFail() {
        mLoadMoreDelegate.loadMoreFail()
    }

    override fun showUpdateInfo(updateInfo: UpdateInfo) {
        // 初始化
        mUpdateUtil = UpdateUtil(this)
        // 开始更新
        mUpdateUtil
            ?.showDownloadNotification(
                getString(R.string.upgrade__upgrading),
                getString(R.string.upgrade__downloading)
            )
            ?.showDownloadProgressDialog(
                getString(R.string.upgrade__downloading),
                getString(R.string.upgrade__background_download),
                getString(R.string.upgrade__cancel)
            )
            ?.start(
                updateInfo.apkFileUrl,
                updateInfo.clientVersion,
                updateInfo.clientLog,
                getString(R.string.upgrade__start),
                getString(R.string.upgrade__cancel),
                null,
                updateInfo.isForceUpgrade
            )
    }
}