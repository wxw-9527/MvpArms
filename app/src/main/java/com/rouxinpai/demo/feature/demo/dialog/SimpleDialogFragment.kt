package com.rouxinpai.demo.feature.demo.dialog

import androidx.fragment.app.FragmentManager
import com.rouxinpai.arms.base.dialog.BaseDialogFragment
import com.rouxinpai.demo.databinding.SimpleDialogFragmentBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/12/3 10:26
 * desc   :
 */
class SimpleDialogFragment : BaseDialogFragment<SimpleDialogFragmentBinding>() {

    companion object {

        fun builder(): Builder = Builder()
    }

    // Configurable properties
    private var titleText: String? = null
    private var messageText: String? = null
    private var positiveButtonText: String? = null
    private var negativeButtonText: String? = null
    private var onPositiveClick: (() -> Unit)? = null
    private var onNegativeClick: (() -> Unit)? = null

    // Builder pattern for flexible dialog configuration
    class Builder {

        private val dialog = SimpleDialogFragment()

        fun setTitle(title: String): Builder {
            dialog.titleText = title
            return this
        }

        fun setMessage(message: String): Builder {
            dialog.messageText = message
            return this
        }

        fun setPositiveButton(text: String, onClick: (() -> Unit)? = null): Builder {
            dialog.positiveButtonText = text
            dialog.onPositiveClick = onClick
            return this
        }

        fun setNegativeButton(text: String, onClick: (() -> Unit)? = null): Builder {
            dialog.negativeButtonText = text
            dialog.onNegativeClick = onClick
            return this
        }

        fun build(): SimpleDialogFragment = dialog
    }

    // Convenient show method
    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "CustomDialogFragment")
    }
}