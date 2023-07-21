package com.rouxinpai.arms.base.fragment

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.rouxinpai.arms.annotation.EventBusEnabled
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.base.view.ViewDelegate
import com.view.multistatepage.intf.OnRetryClickListener
import org.greenrobot.eventbus.EventBus

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/1/11 10:28
 * desc   :
 */
abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment(), IView, OnRetryClickListener {

    private var mBinding: VB? = null
    val binding: VB get() = mBinding!!

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
        savedInstanceState: Bundle?
    ): View? {
        // 使用指定的布局文件创建视图绑定对象
        mBinding = onCreateViewBinding(inflater, container)
        // 设置对话框背景透明
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 返回根视图
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
        super.onStart()
        dialog?.window?.apply {
            // 获取屏幕宽度
            val screenWidth = resources.displayMetrics.widthPixels
            // 计算目标宽度
            val targetWidth = if (Configuration.ORIENTATION_LANDSCAPE == resources.configuration.orientation) {
                (screenWidth * 0.6f).toInt()
            } else {
                (screenWidth * 0.85f).toInt()
            }
            // 设置对话框的宽度
            setLayout(targetWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onResume() {
        super.onResume()
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

    protected abstract fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup?): VB

    protected open fun onParseData(bundle: Bundle) = Unit

    protected open fun onInit(savedInstanceState: Bundle?) = Unit
}