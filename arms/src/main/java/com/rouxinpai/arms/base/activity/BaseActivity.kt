package com.rouxinpai.arms.base.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.rouxinpai.arms.R
import com.rouxinpai.arms.annotation.EventBusEnabled
import com.rouxinpai.arms.base.application.IApplication
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.receiver.BarcodeScanningReceiver
import com.shashank.sony.fancytoastlib.FancyToast
import com.view.multistatepage.intf.OnRetryClickListener
import com.view.multistatepage.state.EmptyState
import com.view.multistatepage.state.ErrorState
import com.view.multistatepage.state.LoadingState
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState
import com.zy.multistatepage.state.SuccessState
import org.greenrobot.eventbus.EventBus

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/16 14:50
 * desc   :
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), IView, OnRetryClickListener {

    lateinit var binding: VB
        private set

    // 是否使用事件发布-订阅总线
    private var mEventBusEnabled: Boolean = false

    // 加载进度对话框
    private var mKProgressHUD: KProgressHUD? = null

    // 缺省页状态管理实例
    private var mLoadState: MultiStateContainer? = null

    // 扫描结果解析广播
    private lateinit var mReceiver: BarcodeScanningReceiver

    /**
     * 缺省页内容视图
     */
    open val stateLayout: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = onCreateViewBinding(layoutInflater)
        setContentView(binding.root)
        onParseData(intent)
        val bundle = intent?.extras
        if (bundle != null) {
            onParseData(bundle)
        }
        initToolbar()
        mLoadState = stateLayout?.bindMultiState()
        onInit(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        //
        mEventBusEnabled = javaClass.isAnnotationPresent(EventBusEnabled::class.java)
        if (mEventBusEnabled) {
            EventBus.getDefault().register(this)
        }
        // 注册广播
        val intentFilter = IntentFilter().apply { addAction(BarcodeScanningReceiver.ACTION) }
        mReceiver = BarcodeScanningReceiver()
        registerReceiver(mReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        //
        if (mEventBusEnabled) {
            EventBus.getDefault().unregister(this)
        }
        // 取消注册广播
        unregisterReceiver(mReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        dismiss()
    }

    // 初始化标题栏
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (null != toolbar) {
            setSupportActionBar(toolbar)
            if (hideNavButton()) return
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener { onNavigationClick() }
        }
    }

    override fun showToast(messageId: Int, duration: Int, type: Int) {
        val message = getString(messageId)
        showToast(message, duration, type)
    }

    override fun showToast(message: CharSequence?, duration: Int, type: Int) {
        if (message.isNullOrEmpty()) return
        FancyToast.makeText(this, message, duration, type, false).show()
    }

    override fun showProgress(labelId: Int, detailId: Int?) {
        val message = getString(labelId)
        val detailMessage = detailId?.let { getString(it) }
        showProgress(message, detailMessage)
    }

    override fun showProgress(labelMsg: CharSequence?, detailMsg: String?) {
        mKProgressHUD = KProgressHUD
            .create(this, KProgressHUD.Style.SPIN_INDETERMINATE)
            .apply {
                if (labelMsg != null && labelMsg.isNotEmpty()) {
                    setLabel(labelMsg.toString())
                }
                if (detailMsg != null && detailMsg.isNotEmpty()) {
                    setDetailsLabel(detailMsg)
                }
            }
            .setCancellable(true)
            .setDimAmount(0.2f)
            .show()
    }

    override fun updateProgress(detailId: Int) {
        val message = getString(detailId)
        updateProgress(message)
    }

    override fun updateProgress(detailMsg: String?) {
        if (detailMsg.isNullOrEmpty()) return
        if (mKProgressHUD == null) return
        if (false == mKProgressHUD?.isShowing) return
        mKProgressHUD?.setDetailsLabel(detailMsg.toString())
    }

    override fun dismiss() {
        mKProgressHUD?.dismiss()
    }

    override fun showLoadingPage(msgId: Int?, msg: String?, descId: Int?, desc: String?) {
        val loadState = this.mLoadState ?: return
        if (loadState.currentState is SuccessState) return
        val loadingMsg = when {
            msgId != null -> getString(msgId)
            msg != null -> msg
            else -> null
        }
        val loadingDesc = when {
            descId != null -> getString(descId)
            desc != null -> desc
            else -> null
        }
        loadState.show<LoadingState>(false) { state ->
            if (loadingMsg != null) {
                state.setMessage(loadingMsg)
            }
            if (loadingDesc != null) {
                state.setDesc(loadingDesc)
            }
        }
    }

    override fun showEmptyPage(msgId: Int?, msg: String?) {
        val loadState = this.mLoadState ?: return
        val emptyMsg = when {
            msgId != null -> getString(msgId)
            msg != null -> msg
            else -> null
        }
        loadState.show<EmptyState>(false) { state ->
            if (emptyMsg != null) {
                state.setMessage(emptyMsg)
            }
            state.setOnRetryClickListener(this)
        }
    }

    override fun showErrorPage(msgId: Int?, msg: String?, descId: Int?, desc: String?) {
        val loadState = this.mLoadState ?: return
        val errorMsg = when {
            msgId != null -> getString(msgId)
            msg != null -> msg
            else -> null
        }
        val errorDesc = when {
            descId != null -> getString(descId)
            desc != null -> desc
            else -> null
        }
        loadState.show<ErrorState>(false) { state ->
            if (errorMsg != null) {
                state.setMessage(errorMsg)
            }
            if (errorDesc != null) {
                state.setDesc(errorDesc)
            }
            state.setOnRetryClickListener(this)
        }
    }

    override fun showSuccessPage() {
        val loadState = this.mLoadState ?: return
        if (loadState.currentState is SuccessState) return
        loadState.show<SuccessState>()
    }

    override fun loadMoreComplete() {}

    override fun loadMoreEnd(gone: Boolean) {}

    override fun loadMoreFail() {}

    override fun tokenTimeout() {
        val application = application as? IApplication ?: return
        application.onTokenTimeout()
    }

    override fun onRetryClick() = Unit

    protected abstract fun onCreateViewBinding(inflater: LayoutInflater): VB

    protected open fun onParseData(intent: Intent) = Unit

    protected open fun onParseData(bundle: Bundle) = Unit

    protected open fun hideNavButton() = false

    protected open fun onNavigationClick() = onBackPressedDispatcher.onBackPressed()

    protected open fun onInit(savedInstanceState: Bundle?) = Unit
}