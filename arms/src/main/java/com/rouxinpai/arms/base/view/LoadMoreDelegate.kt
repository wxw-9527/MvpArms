package com.rouxinpai.arms.base.view

import com.chad.library.adapter.base.module.BaseLoadMoreModule

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/12 15:35
 * desc   :
 */
class LoadMoreDelegate(loadMoreModule: BaseLoadMoreModule?) : ILoadMore {

    private var mLoadMoreModule: BaseLoadMoreModule? = null

    init {
        mLoadMoreModule = loadMoreModule
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