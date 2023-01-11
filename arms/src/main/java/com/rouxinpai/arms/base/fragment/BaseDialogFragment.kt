package com.rouxinpai.arms.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/1/11 10:28
 * desc   :
 */
abstract class BaseDialogFragment<VB: ViewBinding>: DialogFragment() {

    private var mBinding: VB? = null
    val binding: VB get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = onCreateViewBinding(inflater, container)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 初始化
        onInit(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    protected abstract fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup?): VB

    protected open fun onInit(savedInstanceState: Bundle?) = Unit
}