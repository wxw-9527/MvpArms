@file:Suppress("UNCHECKED_CAST")

package com.rouxinpai.arms.base.dialog

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.module.BaseLoadMoreModule
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
    async: Boolean = false
) : BaseOnBindView<D, VB>(layoutResId, async), ILoadMore {

    // P层实例
    lateinit var presenter: P

    // 加载更多代理类实例
    private lateinit var mLoadMoreDelegate: LoadMoreDelegate

    open val mLoadMoreModule: BaseLoadMoreModule? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        // 初始化Presenter
        presenter = onCreatePresenter(dialog.ownActivity)
        presenter.bind(lifecycle, this as V)
        // 初始化加载更多代理类
        mLoadMoreDelegate = LoadMoreDelegate(mLoadMoreModule)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onBarcodeEvent(event: BarcodeEvent) {
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

    abstract fun onCreatePresenter(activity: Activity): P
}