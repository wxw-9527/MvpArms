package com.rouxinpai.arms.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.rouxinpai.arms.annotation.EventBusEnabled
import com.rouxinpai.arms.base.application.IApplication
import com.rouxinpai.arms.base.view.IView
import com.shashank.sony.fancytoastlib.FancyToast
import com.view.multistatepage.intf.OnRetryClickListener
import com.view.multistatepage.state.EmptyState
import com.view.multistatepage.state.ErrorState
import com.view.multistatepage.state.LoadingState
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState
import com.zy.multistatepage.state.SuccessState
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

    // 加载进度对话框
    private var mKProgressHUD: KProgressHUD? = null

    // 缺省页状态管理实例
    private var mLoadState: MultiStateContainer? = null

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
        mLoadState = stateLayout?.bindMultiState()
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
        val message = getString(messageId)
        showToast(message, duration, type)
    }

    override fun showToast(message: CharSequence?, duration: Int, type: Int) {
        if (message.isNullOrEmpty()) return
        FancyToast.makeText(requireContext(), message, duration, type, false).show()
    }

    override fun showProgress(labelId: Int, detailId: Int?) {
        val message = getString(labelId)
        val detailMessage = detailId?.let { getString(it) }
        showProgress(message, detailMessage)
    }

    override fun showProgress(labelMsg: CharSequence?, detailMsg: String?) {
        mKProgressHUD = KProgressHUD
            .create(requireContext(), KProgressHUD.Style.SPIN_INDETERMINATE)
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

    override fun tokenTimeout() {
        val application = requireActivity().application as? IApplication ?: return
        application.onTokenTimeout()
    }

    override fun onRetryClick() = Unit

    protected abstract fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup?): VB

    protected open fun onParseData(bundle: Bundle) = Unit

    protected open fun onInit(savedInstanceState: Bundle?) = Unit

    protected open fun onLazyInit() = Unit
}