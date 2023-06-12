package com.rouxinpai.arms.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.rouxinpai.arms.annotation.EventBusEnabled
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.base.view.ViewDelegate
import com.view.multistatepage.intf.OnRetryClickListener
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.atomic.AtomicBoolean

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 14:45
 * desc   :
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment(), IView, OnRetryClickListener {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = onCreateViewBinding(inflater, container)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        mViewDelegate = ViewDelegate(requireContext(), stateLayout, this)
        // 初始化
        onInit(savedInstanceState)
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

    override fun showToast(messageId: Int, duration: Int, type: Int) {
        mViewDelegate.showToast(messageId, duration, type)
    }

    override fun showToast(message: CharSequence?, duration: Int, type: Int) {
        mViewDelegate.showToast(message, duration, type)
    }

    override fun showProgress(labelId: Int, detailId: Int?) {
        mViewDelegate.showProgress(labelId, detailId)
    }

    override fun showProgress(labelMsg: CharSequence?, detailMsg: String?) {
        mViewDelegate.showProgress(labelMsg, detailMsg)
    }

    override fun updateProgress(detailId: Int) {
        mViewDelegate.updateProgress(detailId)
    }

    override fun updateProgress(detailMsg: String?) {
        mViewDelegate.updateProgress(detailMsg)
    }

    override fun dismissProgress() {
        mViewDelegate.dismissProgress()
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

    protected abstract fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup?): VB

    protected open fun onParseData(bundle: Bundle) = Unit

    protected open fun onInit(savedInstanceState: Bundle?) = Unit

    protected open fun onLazyInit() = Unit
}