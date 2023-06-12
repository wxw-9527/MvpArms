package com.rouxinpai.arms.base.view

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/12 15:37
 * desc   :
 */
interface ILoadMore {

    /**
     * 本次数据加载完毕
     */
    fun loadMoreComplete() {}

    /**
     * 所有数据加载完成
     *
     * @param gone 是否隐藏加载更多组件
     */
    fun loadMoreEnd(gone: Boolean = false) {}

    /**
     * 本次数据加载错误
     */
    fun loadMoreFail() {}
}