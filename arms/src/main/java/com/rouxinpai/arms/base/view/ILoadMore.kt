package com.rouxinpai.arms.base.view

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/12 15:37
 * desc   :
 */
interface ILoadMore {

    /**
     * 重置“加载更多”状态
     */
    fun resetLoadMoreState() {}

    /**
     * 本次数据加载完毕
     */
    fun loadMoreComplete() {}

    /**
     * 所有数据加载完成
     */
    fun loadMoreEnd() {}

    /**
     * 本次数据加载错误
     */
    fun loadMoreFail(error: Throwable) {}
}