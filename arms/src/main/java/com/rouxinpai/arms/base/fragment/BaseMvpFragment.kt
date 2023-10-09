@file:Suppress("UNCHECKED_CAST")

package com.rouxinpai.arms.base.fragment

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.module.BaseLoadMoreModule
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
 * time   : 2022/11/17 16:26
 * desc   :
 */
abstract class BaseMvpFragment<VB : ViewBinding, V : IView, P : IPresenter<V>> :
    BaseFragment<VB>(), ILoadMore {

    @Inject
    lateinit var presenter: P

    // 加载更多代理类实例
    private lateinit var mLoadMoreDelegate: LoadMoreDelegate

    open val mLoadMoreModule: BaseLoadMoreModule? = null

    override fun onInit(savedInstanceState: Bundle?) {
        presenter.bind(lifecycle, this as V)
        super.onInit(savedInstanceState)
        mLoadMoreDelegate = LoadMoreDelegate(mLoadMoreModule)
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    open fun onBarcodeEvent(event: BarcodeEvent) {
        event.removeStickyEvent()
        presenter.getBarcodeInfo(event.barcode)
    }

    override fun loadMoreComplete() {
        mLoadMoreDelegate.loadMoreComplete()
    }

    override fun loadMoreEnd(gone: Boolean) {
        mLoadMoreDelegate.loadMoreEnd(gone)
    }

    override fun loadMoreFail() {
        mLoadMoreDelegate.loadMoreFail()
    }
}