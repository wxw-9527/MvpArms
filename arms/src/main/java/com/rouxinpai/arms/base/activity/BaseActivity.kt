package com.rouxinpai.arms.base.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.rouxinpai.arms.R
import com.rouxinpai.arms.annotation.EventBusEnabled
import com.rouxinpai.arms.util.BindingReflex
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.base.view.ViewDelegate
import com.view.multistatepage.intf.OnRetryClickListener
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

    // 代理类实例，用于协助处理 IView 接口中定义的各种方法。
    private lateinit var mViewDelegate: ViewDelegate

    /**
     * 缺省页内容视图
     */
    open val stateLayout: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BindingReflex.reflexViewBinding(this::class.java, layoutInflater)
        setContentView(binding.root)
        onParseData(intent)
        val bundle = intent?.extras
        if (bundle != null) {
            onParseData(bundle)
        }
        initToolbar()
        mViewDelegate = ViewDelegate(this, stateLayout, this)
        onInit(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            onParseData(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //
        mEventBusEnabled = javaClass.isAnnotationPresent(EventBusEnabled::class.java)
        if (mEventBusEnabled) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onPause() {
        super.onPause()
        //
        if (mEventBusEnabled) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgress()
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

    override fun showSuccessTip(messageId: Int) {
        mViewDelegate.showSuccessTip(messageId)
    }

    override fun showSuccessTip(message: CharSequence?) {
        mViewDelegate.showSuccessTip(message)
    }

    override fun showWarningTip(messageId: Int) {
        mViewDelegate.showWarningTip(messageId)
    }

    override fun showWarningTip(message: CharSequence?) {
        mViewDelegate.showWarningTip(message)
    }

    override fun showErrorTip(messageId: Int) {
        mViewDelegate.showErrorTip(messageId)
    }

    override fun showErrorTip(message: CharSequence?) {
        mViewDelegate.showErrorTip(message)
    }

    override fun showProgress(messageId: Int?) {
        mViewDelegate.showProgress(messageId)
    }

    override fun showProgress(message: CharSequence?) {
        mViewDelegate.showProgress(message)
    }

    override fun dismissProgress() {
        mViewDelegate.dismissProgress()
    }

    override fun isProgressShowing(): Boolean {
        return mViewDelegate.isProgressShowing()
    }

    override fun showLoadingPage(msgId: Int?, msg: String?, descId: Int?, desc: String?) {
        mViewDelegate.showLoadingPage(msgId, msg, descId, desc)
    }

    override fun showEmptyPage(msgId: Int?, msg: String?) {
        mViewDelegate.showEmptyPage(msgId, msg)
    }

    override fun showErrorPage(msgId: Int?, msg: String?, descId: Int?, desc: String?) {
        mViewDelegate.showErrorPage(msgId, msg, descId, desc)
    }

    override fun showSuccessPage() {
        mViewDelegate.showSuccessPage()
    }

    override fun tokenTimeout() {
        mViewDelegate.tokenTimeout()
    }

    override fun onRetryClick() = Unit

    protected open fun onParseData(intent: Intent) = Unit

    protected open fun onParseData(bundle: Bundle) = Unit

    protected open fun hideNavButton() = false

    protected open fun onNavigationClick() = onBackPressedDispatcher.onBackPressed()

    protected open fun onInit(savedInstanceState: Bundle?) = Unit
}