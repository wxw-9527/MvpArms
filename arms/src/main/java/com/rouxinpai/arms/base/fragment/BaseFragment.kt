package com.rouxinpai.arms.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.rouxinpai.arms.base.application.IApplication
import com.rouxinpai.arms.base.view.IView
import com.shashank.sony.fancytoastlib.FancyToast
import com.view.multistatepage.MultiStateContainer
import com.view.multistatepage.intf.OnRetryClickListener
import com.view.multistatepage.state.EmptyState
import com.view.multistatepage.state.ErrorState
import com.view.multistatepage.state.LoadingState
import com.view.multistatepage.state.SuccessState
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

    // 加载进度对话框
    private var mKProgressHUD: KProgressHUD? = null

    /**
     * 缺省页实例
     */
    abstract val loadState: MultiStateContainer?

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
        mBinding = onCreateViewBinding(inflater, container)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 初始化
        onInit(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (mInitialized.compareAndSet(false, true)) {
            onLazyInit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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

    override fun showProgress(messageId: Int) {
        val message = getString(messageId)
        showProgress(message)
    }

    override fun showProgress(message: CharSequence?) {
        mKProgressHUD = KProgressHUD
            .create(requireContext(), KProgressHUD.Style.SPIN_INDETERMINATE)
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
        val application = requireActivity().application as? IApplication ?: return
        application.onTokenTimeout()
    }

    override fun onRetryClick() = Unit

    protected abstract fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup?): VB

    protected open fun onParseData(bundle: Bundle) = Unit

    protected open fun onInit(savedInstanceState: Bundle?) = Unit

    protected open fun onLazyInit() = Unit
}