package com.rouxinpai.demo.feature.demo.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.rouxinpai.arms.base.fragment.BaseBottomSheetDialogFragment
import com.rouxinpai.demo.databinding.SimpleBottomDialogFragmentBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/7/17 14:48
 * desc   :
 */
class SimpleBottomDialogFragment :
    BaseBottomSheetDialogFragment<SimpleBottomDialogFragmentBinding>() {

    companion object {

        /**
         * 显示简单底部对话框
         */
        fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
            SimpleBottomDialogFragment().show(
                fragmentManager,
                SimpleBottomDialogFragment::class.java.simpleName
            )
        }
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): SimpleBottomDialogFragmentBinding {
        return SimpleBottomDialogFragmentBinding.inflate(inflater, parent, false)
    }

}