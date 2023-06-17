package com.rouxinpai.arms.base.view

import android.content.Context
import android.view.View
import com.kaopiz.kprogresshud.KProgressHUD
import com.rouxinpai.arms.base.application.IApplication
import com.shashank.sony.fancytoastlib.FancyToast
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

    // 加载进度对话框
    private var mKProgressHUD: KProgressHUD? = null

    // 缺省页状态管理实例
    private var mLoadState: MultiStateContainer? = null

    // 重试点击事件
    private var mOnRetryClickListener: OnRetryClickListener? = null

    init {
        mContext = context
        mLoadState = stateLayout?.bindMultiState()
        mOnRetryClickListener = retryClickListener
    }

    override fun showToast(messageId: Int, duration: Int, type: Int) {
        val message = mContext.getString(messageId)
        showToast(message, duration, type)
    }

    override fun showToast(message: CharSequence?, duration: Int, type: Int) {
        if (message.isNullOrEmpty()) return
        FancyToast.makeText(mContext, message, duration, type, false).show()
    }

    override fun showProgress(labelId: Int, detailId: Int?) {
        val message = mContext.getString(labelId)
        val detailMessage = detailId?.let { mContext.getString(it) }
        showProgress(message, detailMessage)
    }

    override fun showProgress(labelMsg: CharSequence?, detailMsg: String?) {
        mKProgressHUD = KProgressHUD
            .create(mContext, KProgressHUD.Style.SPIN_INDETERMINATE)
            .apply {
                if (!labelMsg.isNullOrEmpty()) {
                    setLabel(labelMsg.toString())
                }
                if (!detailMsg.isNullOrEmpty()) {
                    setDetailsLabel(detailMsg)
                }
            }
            .setCancellable(true)
            .setDimAmount(0.2f)
            .show()
    }

    override fun updateProgress(detailId: Int) {
        val message = mContext.getString(detailId)
        updateProgress(message)
    }

    override fun updateProgress(detailMsg: String?) {
        if (detailMsg.isNullOrEmpty()) return
        if (mKProgressHUD == null) return
        if (false == mKProgressHUD?.isShowing) return
        mKProgressHUD?.setDetailsLabel(detailMsg.toString())
    }

    override fun dismissProgress() {
        mKProgressHUD?.dismiss()
        mKProgressHUD = null
    }

    override fun isProgressShowing(): Boolean {
        return mKProgressHUD?.isShowing ?: false
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