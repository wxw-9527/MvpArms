package com.rouxinpai.demo.feature.demo.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.rouxinpai.arms.base.fragment.BaseDialogFragment
import com.rouxinpai.demo.databinding.SimpleDialogFragmentBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/7/17 13:58
 * desc   :
 */
class SimpleDialogFragment : BaseDialogFragment<SimpleDialogFragmentBinding>() {

    companion object {

        /**
         * 显示对话框
         */
        fun show(manager: FragmentManager) = SimpleDialogFragment().apply {
            show(manager, SimpleDialogFragment::class.java.simpleName)
        }
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): SimpleDialogFragmentBinding {
        return SimpleDialogFragmentBinding.inflate(inflater, parent, false)
    }
}