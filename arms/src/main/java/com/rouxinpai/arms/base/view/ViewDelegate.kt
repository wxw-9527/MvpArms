package com.rouxinpai.arms.base.view

import android.content.Context
import android.view.View
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.dialogs.WaitDialog
import com.rouxinpai.arms.base.application.BaseApplication
import com.view.multistatepage.intf.OnRetryClickListener
import com.view.multistatepage.state.EmptyState
import com.view.multistatepage.state.ErrorState
import com.view.multistatepage.state.LoadingState
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState
import com.zy.multistatepage.state.SuccessState
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/12 15:15
 * desc   :
 */
class ViewDelegate(private val context: Context, stateLayout: View?, retryClickListener: OnRetryClickListener, ) :
    IView {

    // 缺省页状态管理实例
    private var mLoadState: MultiStateContainer? = null

    // 重试点击事件
    private var mOnRetryClickListener: OnRetryClickListener? = null

    init {
        mLoadState = stateLayout?.bindMultiState()
        mOnRetryClickListener = retryClickListener
    }

    override fun showSuccessTip(messageId: Int) {
        val message = context.getString(messageId)
        showSuccessTip(message)
    }

    override fun showSuccessTip(message: CharSequence?) {
        PopTip.show(message)
            .iconSuccess()
            .autoDismiss(1000L)
    }

    override fun showWarningTip(messageId: Int) {
        val message = context.getString(messageId)
        showWarningTip(message)
    }

    override fun showWarningTip(message: CharSequence?) {
        PopTip.show(message)
            .iconWarning()
            .showLong()
    }

    override fun showErrorTip(messageId: Int) {
        val message = context.getString(messageId)
        showErrorTip(message)
    }

    override fun showErrorTip(message: CharSequence?) {
        PopTip.show(message)
            .iconError()
            .showLong()
    }

    override fun showProgress(messageId: Int?) {
        val message = messageId?.let { context.getString(it) }
        showProgress(message)
    }

    override fun showProgress(message: CharSequence?) {
        WaitDialog.show(message)
    }

    override fun dismissProgress() {
        WaitDialog.dismiss()
    }

    override fun isProgressShowing(): Boolean {
        return WaitDialog.getInstance().isShow
    }

    override fun showLoadingPage(msgId: Int?, msg: String?, descId: Int?, desc: String?) {
        val loadState = this.mLoadState ?: return
        if (loadState.currentState is SuccessState) return
        val loadingMsg = when {
            msgId != null -> context.getString(msgId)
            msg != null -> msg
            else -> null
        }
        val loadingDesc = when {
            descId != null -> context.getString(descId)
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
            msgId != null -> context.getString(msgId)
            msg != null -> msg
            else -> null
        }
        loadState.show<EmptyState>(false) { state ->
            if (emptyMsg != null) {
                state.setMessage(emptyMsg)
            }
            state.setOnRetryClickListener(mOnRetryClickListener)
        }
    }

    override fun showErrorPage(msgId: Int?, msg: String?, descId: Int?, desc: String?) {
        val loadState = this.mLoadState ?: return
        val errorMsg = when {
            msgId != null -> context.getString(msgId)
            msg != null -> msg
            else -> null
        }
        val errorDesc = when {
            descId != null -> context.getString(descId)
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
            state.setOnRetryClickListener(mOnRetryClickListener)
        }
    }

    override fun showSuccessPage() {
        val loadState = this.mLoadState ?: return
        if (loadState.currentState is SuccessState) return
        loadState.show<SuccessState>()
    }

    // 原子布尔值，用于确保线程安全
    private val mIsHandlingTokenTimeout = AtomicBoolean(false)
    // 记录上次调用的时间
    private var mLastTimeoutTime: Long = 0L
    // 节流时间，单位是毫秒
    private val mThrottleTime = 2_000L

    override fun tokenTimeout() {
        val currentTime = System.currentTimeMillis()
        if (mIsHandlingTokenTimeout.compareAndSet(false, true) && (currentTime - mLastTimeoutTime > mThrottleTime)) {
            thread {
                try {
                    val baseApp = (context.applicationContext as? BaseApplication)
                    baseApp?.onTokenTimeout()
                    Thread.sleep(mThrottleTime)
                } finally {
                    // 重置标志位
                    mIsHandlingTokenTimeout.set(false)
                }
            }
        }
    }
}