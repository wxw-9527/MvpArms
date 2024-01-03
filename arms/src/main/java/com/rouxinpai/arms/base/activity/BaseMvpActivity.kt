@file:Suppress("UNCHECKED_CAST")

package com.rouxinpai.arms.base.activity

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.PathUtils
import com.chad.library.adapter4.QuickAdapterHelper
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.rouxinpai.arms.R
import com.rouxinpai.arms.annotation.BarcodeScanningReceiverEnabled
import com.rouxinpai.arms.barcode.event.BarcodeEvent
import com.rouxinpai.arms.barcode.receiver.BarcodeScanningReceiver
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.ILoadMore
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.base.view.LoadMoreDelegate
import com.rouxinpai.arms.update.model.UpdateInfo
import com.rouxinpai.arms.nfc.util.NfcUtil
import com.rouxinpai.arms.util.DownloadUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
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

    // NFC相关
    private var mNfcAdapter: NfcAdapter? = null

    // 扫描结果解析广播
    private lateinit var mReceiver: BarcodeScanningReceiver

    // 工具类，用于构造“加载更多”、头部尾部、组合Adapter等功能
    var adapterHelper: QuickAdapterHelper? = null

    // 加载更多代理类实例
    private lateinit var mLoadMoreDelegate: LoadMoreDelegate

    override fun onInit(savedInstanceState: Bundle?) {
        // 绑定生命周期
        presenter.bind(lifecycle, this as V)
        // 初始化加载更多代理类
        adapterHelper = onCreateAdapterHelper()
        if (adapterHelper != null) {
            mLoadMoreDelegate = LoadMoreDelegate(adapterHelper)
        }
        super.onInit(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mBarcodeScanningReceiverEnabled = javaClass.isAnnotationPresent(BarcodeScanningReceiverEnabled::class.java)
        if (mBarcodeScanningReceiverEnabled) {
            // 初始化NFC相关
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
            val intent = Intent(this, javaClass)
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_ONE_SHOT
            val nfcPendingIntent = PendingIntent.getActivity(this, 0, intent, flags)
            // 设置当该页面处于前台时，NFC标签会直接交给该页面处理
            mNfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
            // 注册广播
            val intentFilter = IntentFilter().apply {
                BarcodeScanningReceiver.sActions.forEach { action -> addAction(action) }
            }
            mReceiver = BarcodeScanningReceiver()
            registerReceiver(mReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        if (mBarcodeScanningReceiverEnabled) {
            // 当页面不可见时，NFC标签不交给当前页面处理
            mNfcAdapter?.disableForegroundDispatch(this)
            // 取消注册广播
            unregisterReceiver(mReceiver)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        onParseNfcIntent(intent)
    }

    /**
     * 解析处理NFC意图
     */
    open fun onParseNfcIntent(intent: Intent?) {
        val ndefText = NfcUtil.readNfcTag(intent)
        // 将字符串前指定长度去除，获得真实的字符串
        val text = ndefText?.substring(NfcUtil.FLAG_LENGTH)
        if (text == null) {
            showWarningTip(R.string.nfc__tag_empty)
            return
        }
        BarcodeEvent.postSticky(text)
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true, priority = 0)
    open fun onBarcodeEvent(event: BarcodeEvent) {
        event.removeStickyEvent()
        presenter.getBarcodeInfo(event.barcode)
    }

    override fun resetLoadMoreState() {
        mLoadMoreDelegate.resetLoadMoreState()
    }

    override fun loadMoreComplete() {
        mLoadMoreDelegate.loadMoreComplete()
    }

    override fun loadMoreEnd() {
        mLoadMoreDelegate.loadMoreEnd()
    }

    override fun loadMoreFail(error: Throwable) {
        mLoadMoreDelegate.loadMoreFail(error)
    }

    override fun showUpdateInfo(updateInfo: UpdateInfo) {
        MessageDialog
            .show(
                updateInfo.clientVersion,
                updateInfo.clientLog,
            )
            .setCancelable(false)
            .setCancelButton(R.string.upgrade__cancel)
            .setOkButton(R.string.upgrade__start) { _, _ ->
                val url = updateInfo.apkFileUrl
                val apkPath = PathUtils.getExternalAppFilesPath() + File.separator + packageName
                DownloadUtil.getInstance()
                    .startDownload(url, apkPath, mOnDownloadListener)
                false
            }
    }

    // 版本更新状态监听
    private val mOnDownloadListener = object : DownloadUtil.OnDownloadListener {

        override fun onDownloadStart() {
            WaitDialog.show(R.string.upgrade__downloading)
        }

        override fun onDownloading(percent: Int) {
            WaitDialog.show(R.string.upgrade__downloading, percent.toFloat() / 100)
        }

        override fun onDownloadFail(e: Exception?) {
            WaitDialog.dismiss()
            showWarningTip(R.string.upgrade__download_fail)
        }

        override fun onDownloadComplete(file: File) {
            WaitDialog.dismiss()
            AppUtils.installApp(file)
        }
    }

    /**
     * 创建QuickAdapterHelper实例
     */
    open fun onCreateAdapterHelper(): QuickAdapterHelper? = null
}