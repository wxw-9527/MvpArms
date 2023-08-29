package com.rouxinpai.demo.feature.demo.dialog

import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import androidx.lifecycle.LifecycleOwner
import com.kongzue.dialogx.dialogs.BottomDialog
import com.kongzue.dialogx.dialogs.CustomDialog
import com.kongzue.dialogx.interfaces.BaseDialog
import com.rouxinpai.arms.base.dialog.BaseOnBindView
import com.rouxinpai.demo.R
import com.rouxinpai.demo.databinding.SimpleCustomViewDialogBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/16 15:53
 * desc   :
 */
class SimpleCustomViewDialog<D : BaseDialog> :
    BaseOnBindView<D, SimpleCustomViewDialogBinding>(R.layout.simple_custom_view_dialog) {

    companion object {

        /**
         * 展示简单自定义弹窗
         */
        fun showBottomDialog() {
            BottomDialog.build()
                .setCustomView(SimpleCustomViewDialog<BottomDialog>())
                .show()
        }

        /**
         * 展示简单自定义弹窗
         */
        fun showCustomDialog() {
            CustomDialog
                .build()
                .setCustomView(SimpleCustomViewDialog<CustomDialog>())
                .show()
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        binding.btnSure.setOnClickListener {
            showProgress()
            Handler(Looper.getMainLooper()).postDelayed(2000L) {
                dismissProgress()
                showSuccessTip("确认提交成功")
            }
        }
    }
}