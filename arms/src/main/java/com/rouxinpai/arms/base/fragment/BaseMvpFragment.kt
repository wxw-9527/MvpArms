package com.rouxinpai.arms.base.fragment

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.module.BaseLoadMoreModule
import com.rouxinpai.arms.barcode.event.BarcodeEvent
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
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
    BaseFragment<VB>() {

    @Inject
    lateinit var presenter: P

    open val mLoadMoreModule: BaseLoadMoreModule? = null

    override fun onInit(savedInstanceState: Bundle?) {
        @Suppress("UNCHECKED_CAST")
        presenter.bind(lifecycle, this as V)
        super.onInit(savedInstanceState)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onBarcodeEvent(event: BarcodeEvent) {
        presenter.getBarcodeInfo(event.barcode)
    }

    override fun loadMoreComplete() {
        super.loadMoreComplete()
        mLoadMoreModule?.loadMoreComplete()
    }

    override fun loadMoreEnd(gone: Boolean) {
        super.loadMoreEnd(gone)
        mLoadMoreModule?.loadMoreEnd(gone)
    }

    override fun loadMoreFail() {
        super.loadMoreFail()
        mLoadMoreModule?.loadMoreFail()
    }
}