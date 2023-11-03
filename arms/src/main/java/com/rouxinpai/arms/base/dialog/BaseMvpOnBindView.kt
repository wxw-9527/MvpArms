@file:Suppress("UNCHECKED_CAST")

package com.rouxinpai.arms.base.dialog

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.QuickAdapterHelper
import com.kongzue.dialogx.interfaces.BaseDialog
import com.rouxinpai.arms.barcode.event.BarcodeEvent
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.ILoadMore
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.base.view.LoadMoreDelegate
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/16 16:48
 * desc   :
 */
abstract class BaseMvpOnBindView<D : BaseDialog, VB : ViewBinding, V : IView, P : IPresenter<V>>(
    layoutResId: Int,
    async: Boolean = false,
) : BaseOnBindView<D, VB>(layoutResId, async), ILoadMore {

    // P层实例
    lateinit var presenter: P

    // 工具类，用于构造“加载更多”、头部尾部、组合Adapter等功能
    var adapterHelper: QuickAdapterHelper? = null

    // 加载更多代理类实例
    private lateinit var mLoadMoreDelegate: LoadMoreDelegate

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        // 初始化Presenter
        presenter = onCreatePresenter(dialog.ownActivity)
        presenter.bind(lifecycle, this as V)
        // 初始化加载更多代理类
        adapterHelper = onCreateAdapterHelper()
        if (adapterHelper != null) {
            mLoadMoreDelegate = LoadMoreDelegate(adapterHelper)
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true, priority = 2)
    open fun onBarcodeEvent(event: BarcodeEvent) {
        event.removeStickyEvent()
        presenter.getBarcodeInfo(event.barcode)
    }

    override fun resetLoadMoreState() {
        mLoadMoreDelegate.resetLoadMoreState()
    }

    override fun loadMoreComplete() {
        mLoadMoreDelegate.loadMoreComplete()
    }

    override fun loadMoreEnd() {
        mLoadMoreDelegate.loadMoreEnd()
    }

    override fun loadMoreFail(error: Throwable) {
        mLoadMoreDelegate.loadMoreFail(error)
    }

    abstract fun onCreatePresenter(activity: Activity): P

    /**
     * 创建QuickAdapterHelper实例
     */
    open fun onCreateAdapterHelper(): QuickAdapterHelper? = null
}