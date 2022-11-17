package com.rouxinpai.arms.base.fragment

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 16:26
 * desc   :
 */
abstract class BaseMvpFragment<VB : ViewBinding, V : IView, P : IPresenter<V>> :
    BaseFragment<VB>() {

    @Inject
    lateinit var presenter: P

    override fun onInit(savedInstanceState: Bundle?) {
        @Suppress("UNCHECKED_CAST")
        presenter.bind(lifecycle, this as V)
        super.onInit(savedInstanceState)
    }
}