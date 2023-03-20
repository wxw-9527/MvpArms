package com.rouxinpai.arms.base.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.module.BaseLoadMoreModule
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 15:48
 * desc   :
 */
abstract class BaseMvpActivity<VB : ViewBinding, V : IView, P : IPresenter<V>> :
    BaseActivity<VB>() {

    @Inject
    lateinit var presenter: P

    open val mLoadMoreModule: BaseLoadMoreModule? = null

    override fun onInit(savedInstanceState: Bundle?) {
        @Suppress("UNCHECKED_CAST")
        presenter.bind(lifecycle, this as V)
        super.onInit(savedInstanceState)
    }

    override fun loadMoreComplete() {
        mLoadMoreModule?.loadMoreComplete()
    }

    override fun loadMoreEnd(gone: Boolean) {
        mLoadMoreModule?.loadMoreEnd(gone)
    }

    override fun loadMoreFail() {
        mLoadMoreModule?.loadMoreFail()
    }
}