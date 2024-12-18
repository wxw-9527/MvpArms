package com.rouxinpai.arms.base.dialog

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter4.QuickAdapterHelper
import com.rouxinpai.arms.barcode.event.BarcodeEvent
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.ILoadMore
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.base.view.LoadMoreDelegate
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/12/3 10:47
 * desc   :
 */
abstract class BaseMvpDialogFragment<VB : ViewBinding, V : IView, P : IPresenter<V>>: BaseDialogFragment<VB>(),
    ILoadMore {

    @Inject
    lateinit var presenter: P

    // 工具类，用于构造“加载更多”、头部尾部、组合Adapter等功能
    var adapterHelper: QuickAdapterHelper? = null

    // 加载更多代理类实例
    private lateinit var mLoadMoreDelegate: LoadMoreDelegate

    override fun onInit(savedInstanceState: Bundle?) {
        // 绑定生命周期
        presenter.bind(lifecycle, this as V)
        // 初始化加载更多代理类
        adapterHelper = onCreateAdapterHelper()
        if (adapterHelper != null) {
            mLoadMoreDelegate = LoadMoreDelegate(adapterHelper)
        }
        super.onInit(savedInstanceState)
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true, priority = 1)
    open fun onBarcodeEvent(event: BarcodeEvent) {
        if (event.removeStickyEvent()) {
            presenter.getBarcodeInfo(event.barcode)
        }
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

    /**
     * 创建QuickAdapterHelper实例
     */
    open fun onCreateAdapterHelper(): QuickAdapterHelper? = null
}