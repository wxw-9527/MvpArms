package com.rouxinpai.arms.base.view

import android.content.Context
import android.view.View
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.dialogs.WaitDialog
import com.rouxinpai.arms.base.application.IApplication
import com.view.multistatepage.intf.OnRetryClickListener
import com.view.multistatepage.state.EmptyState
import com.view.multistatepage.state.ErrorState
import com.view.multistatepage.state.LoadingState
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState
import com.zy.multistatepage.state.SuccessState

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/12 15:15
 * desc   :
 */
class ViewDelegate(context: Context, stateLayout: View?, retryClickListener: OnRetryClickListener) :
    IView {

    // 上下文对象
    private var mContext: Context

    // 缺省页状态管理实例
    private var mLoadState: MultiStateContainer? = null

    // 重试点击事件
    private var mOnRetryClickListener: OnRetryClickListener? = null

    init {
        mContext = context
        mLoadState = stateLayout?.bindMultiState()
        mOnRetryClickListener = retryClickListener
    }

    override fun showSuccessTip(messageId: Int) {
        val message = mContext.getString(messageId)
        showSuccessTip(message)
    }

    override fun showSuccessTip(message: CharSequence?) {
        PopTip.show(message)
            .iconSuccess()
            .autoDismiss(1000L)
    }

    override fun showWarningTip(messageId: Int) {
        val message = mContext.getString(messageId)
        showWarningTip(message)
    }

    override fun showWarningTip(message: CharSequence?) {
        PopTip.show(message)
            .iconWarning()
            .showLong()
    }

    override fun showErrorTip(messageId: Int) {
        val message = mContext.getString(messageId)
        showErrorTip(message)
    }

    override fun showErrorTip(message: CharSequence?) {
        PopTip.show(message)
            .iconError()
            .showLong()
    }

    override fun showProgress(messageId: Int?) {
        val message = messageId?.let { mContext.getString(it) }
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
            msgId != null -> mContext.getString(msgId)
            msg != null -> msg
            else -> null
        }
        val loadingDesc = when {
            descId != null -> mContext.getString(descId)
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
            msgId != null -> mContext.getString(msgId)
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
            msgId != null -> mContext.getString(msgId)
            msg != null -> msg
            else -> null
        }
        val errorDesc = when {
            descId != null -> mContext.getString(descId)
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

    override fun tokenTimeout() {
        val application = mContext.applicationContext as? IApplication ?: return
        application.onTokenTimeout()
    }
}