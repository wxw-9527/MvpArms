package com.rouxinpai.arms.base.presenter

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 14:52
 * desc   :
 */
interface IPresenter<V : IView> : DefaultLifecycleObserver {

    fun bind(lifecycle: Lifecycle, v: V)
}