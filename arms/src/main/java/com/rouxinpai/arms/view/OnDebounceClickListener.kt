package com.rouxinpai.arms.view

import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/3 15:25
 * desc   :
 */
abstract class OnDebounceClickListener(
    private val lifecycle: Lifecycle,
    private val delayTime: Long = 1000L,
) : OnClickListener {

    private var mDisposable: Disposable? = null

    init {
        // 注册生命周期
        val defaultLifecycleObserver = object : DefaultLifecycleObserver {

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                //
                lifecycle.removeObserver(this)
                // 释放资源
                mDisposable?.dispose()
                mDisposable = null
            }
        }
        //
        lifecycle.addObserver(defaultLifecycleObserver)
    }


    override fun onClick(v: View?) {
        mDisposable = v?.clicks()
            ?.throttleFirst(delayTime, TimeUnit.MILLISECONDS) // 1秒以内第一次点击事件有效
            ?.subscribe {
                onDebounceClick(v)
            }
    }

    /**
     * 防抖点击事件回调方法
     * @param v
     */
    abstract fun onDebounceClick(v: View?)
}