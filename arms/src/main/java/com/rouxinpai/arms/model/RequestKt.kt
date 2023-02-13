package com.rouxinpai.arms.model

import com.rouxinpai.arms.base.presenter.BasePresenter

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 15:05
 * desc   :
 */

inline fun <T> BasePresenter<*>.request(block: Request<T>.() -> Unit) {
    Request<T>().apply(block).request(this, view)
}