package com.rouxinpai.arms.base.view

import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/12 15:35
 * desc   :
 */
class LoadMoreDelegate(private val helper: QuickAdapterHelper?) : ILoadMore {

    override fun resetLoadMoreState() {
        helper?.trailingLoadState = LoadState.None
    }

    override fun loadMoreComplete() {
        helper?.trailingLoadState = LoadState.NotLoading(false)
    }

    override fun loadMoreEnd() {
        helper?.trailingLoadState = LoadState.NotLoading(true)
    }

    override fun loadMoreFail(error: Throwable) {
        helper?.trailingLoadState = LoadState.Error(error)
    }
}