package com.rouxinpai.arms.base.dialog

import android.content.Context
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.kongzue.dialogx.interfaces.BaseDialog
import com.kongzue.dialogx.interfaces.OnBindView
import com.rouxinpai.arms.annotation.EventBusEnabled
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.base.view.ViewDelegate
import com.view.multistatepage.intf.OnRetryClickListener
import org.greenrobot.eventbus.EventBus

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/16 15:00
 * desc   :
 */
abstract class BaseOnBindView<D : BaseDialog, VB : ViewBinding>(
    layoutResId: Int,
    async: Boolean = false
) : OnBindView<D>(layoutResId, async),
    DefaultLifecycleObserver,
    IView,
    OnRetryClickListener {

    // 生命周期管理实例
    lateinit var lifecycle: Lifecycle

    // 对话框实例
    lateinit var dialog: D

    // 视图绑定实例
    lateinit var binding: VB

    // 是否使用事件发布-订阅总线
    private var mEventBusEnabled: Boolean = false

    // 代理类实例，用于协助处理 IView 接口中定义的各种方法。
    private lateinit var mViewDelegate: ViewDelegate

    /**
     * 缺省页内容视图
     */
    open val stateLayout: View? = null

    /**
     * 获取上下文对象
     */
    val context: Context
        get() = dialog.ownActivity

    override fun onBind(dialog: D, v: View) {
        // 初始化
        this.lifecycle = dialog.lifecycle
        this.dialog = dialog
        this.binding = onBindView(v)
        // 绑定生命周期方法
        lifecycle.addObserver(this)
        // 初始化代理类
        mViewDelegate = ViewDelegate(context, stateLayout, this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        // 事件发布-订阅总线
        mEventBusEnabled = javaClass.isAnnotationPresent(EventBusEnabled::class.java)
        if (mEventBusEnabled) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        lifecycle.removeObserver(this)
        super.onDestroy(owner)
        if (mEventBusEnabled) {
            EventBus.getDefault().unregister(this)
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

    abstract fun onBindView(view: View): VB
}