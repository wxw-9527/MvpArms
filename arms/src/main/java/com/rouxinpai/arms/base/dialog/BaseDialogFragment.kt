package com.rouxinpai.arms.base.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.rouxinpai.arms.annotation.EventBusEnabled
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.base.view.ViewDelegate
import com.rouxinpai.arms.util.BindingReflex
import com.view.multistatepage.intf.OnRetryClickListener
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.atomic.AtomicBoolean

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/12/3 10:20
 * desc   :
 */
abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment(), IView,
    OnRetryClickListener {

    private var mBinding: VB? = null
    val binding: VB get() = mBinding!!

    // 初始化标识
    private val mInitialized = AtomicBoolean(false)
    val initialized get() = mInitialized

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
        // 解析传递至本页的数据
        val bundle = arguments
        if (bundle != null) {
            onParseData(bundle)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = BindingReflex.reflexViewBinding(this::class.java, layoutInflater, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        mViewDelegate = ViewDelegate(requireContext(), stateLayout, this)
        // 初始化
        onInit(savedInstanceState)
    }

    override fun onStart() {
        dialog?.window?.apply {
            // 设置对话框宽度和背景
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        if (mInitialized.compareAndSet(false, true)) {
            onLazyInit()
        }
        // 事件发布-订阅总线
        mEventBusEnabled = javaClass.isAnnotationPresent(EventBusEnabled::class.java)
        if (mEventBusEnabled) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onPause() {
        super.onPause()
        if (mEventBusEnabled) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
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

    protected open fun onParseData(bundle: Bundle) = Unit

    protected open fun onInit(savedInstanceState: Bundle?) = Unit

    protected open fun onLazyInit() = Unit
}