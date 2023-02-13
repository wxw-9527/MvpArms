package com.rouxinpai.arms.base.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.rouxinpai.arms.base.view.IView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 14:53
 * desc   :
 */
abstract class BasePresenter<V : IView> : IPresenter<V> {

    @Inject
    lateinit var retrofit: Retrofit

    private var mLifecycle: Lifecycle? = null

    private val mDisposable = CompositeDisposable()

    var view: V? = null
        private set

    override fun bind(lifecycle: Lifecycle, v: V) {
        // 绑定view引用
        this.view = v
        // 注册生命周期监听
        this.mLifecycle = lifecycle
        this.mLifecycle?.addObserver(this)
    }

    override fun addDisposable(disposable: Disposable) {
        mDisposable.add(disposable)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        // 统一取消网络请求
        mDisposable.clear()
        // 移除生命周期监听
        mLifecycle?.removeObserver(this)
        mLifecycle = null
        // 销毁view引用
        view = null
        super.onDestroy(owner)
    }
}