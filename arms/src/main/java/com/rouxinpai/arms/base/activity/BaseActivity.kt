package com.rouxinpai.arms.base.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.application.IApplication
import com.rouxinpai.arms.base.view.IView
import com.shashank.sony.fancytoastlib.FancyToast
import com.view.multistatepage.MultiStateContainer
import com.view.multistatepage.intf.OnRetryClickListener
import com.view.multistatepage.state.EmptyState
import com.view.multistatepage.state.ErrorState
import com.view.multistatepage.state.LoadingState
import com.view.multistatepage.state.SuccessState

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/16 14:50
 * desc   :
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), IView, OnRetryClickListener {

    lateinit var binding: VB
        private set

    // 加载进度对话框
    private var mKProgressHUD: KProgressHUD? = null

    /**
     * 缺省页实例
     */
    open val loadState: MultiStateContainer? = null

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
        onInit(savedInstanceState)
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

    override fun showProgress(messageId: Int) {
        val message = getString(messageId)
        showProgress(message)
    }

    override fun showProgress(message: CharSequence?) {
        mKProgressHUD = KProgressHUD
            .create(this, KProgressHUD.Style.SPIN_INDETERMINATE)
            .apply {
                if (message != null && message.isNotEmpty()) {
                    setLabel(message.toString())
                }
            }
            .setCancellable(true)
            .setDimAmount(0.2f)
            .show()
    }

    override fun dismiss() {
        mKProgressHUD?.dismiss()
    }

    override fun showLoadingPage(msgId: Int?, msg: String?, descId: Int?, desc: String?) {
        val loadState = this.loadState ?: return
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
        val loadState = this.loadState ?: return
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
        val loadState = this.loadState ?: return
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
        val loadState = this.loadState ?: return
        if (loadState.currentState is SuccessState) return
        loadState.show<SuccessState>()
    }

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