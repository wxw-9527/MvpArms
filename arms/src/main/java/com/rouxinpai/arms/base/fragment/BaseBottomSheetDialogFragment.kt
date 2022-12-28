package com.rouxinpai.arms.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/12/28 14:45
 * desc   :
 */
abstract class BaseBottomSheetDialogFragment<VB : ViewBinding>: BottomSheetDialogFragment() {

    private var mBinding: VB? = null
    val binding: VB get() = mBinding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext())
    }

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