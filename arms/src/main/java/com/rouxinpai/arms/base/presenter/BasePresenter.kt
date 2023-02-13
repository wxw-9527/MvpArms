package com.rouxinpai.arms.base.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.rouxinpai.arms.base.view.IView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 14:53
 * desc   :
 */
abstract class BasePresenter<V : IView> : IPresenter<V>,
    CoroutineScope by CoroutineScope(Job() + Dispatchers.Main) {

    @Inject
    lateinit var retrofit: Retrofit

    private var mLifecycle: Lifecycle? = null

    var view: V? = null
        private set

    override fun bind(lifecycle: Lifecycle, v: V) {
        this.mLifecycle = lifecycle
        this.view = v
        this.mLifecycle?.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        cancel()
        mLifecycle?.removeObserver(this)
        mLifecycle = null
        view = null
        super.onDestroy(owner)
    }
}