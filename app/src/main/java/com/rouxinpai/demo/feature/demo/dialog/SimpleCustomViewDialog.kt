package com.rouxinpai.demo.feature.demo.dialog

import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import androidx.lifecycle.LifecycleOwner
import com.kongzue.dialogx.dialogs.BottomDialog
import com.rouxinpai.arms.base.dialog.BaseOnBindView
import com.rouxinpai.demo.databinding.SimpleCustomViewDialogBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/16 15:53
 * desc   :
 */
class SimpleCustomViewDialog :
    BaseOnBindView<BottomDialog, SimpleCustomViewDialogBinding>() {

    companion object {

        /**
         * 展示简单自定义弹窗
         */
        fun showBottomDialog() {
            BottomDialog.build()
                .setCustomView(SimpleCustomViewDialog())
                .show()
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        dialog.setOkButton("确认") { _, _ ->
            showProgress()
            Handler(Looper.getMainLooper()).postDelayed(2000L) {
                dismissProgress()
                showSuccessTip("确认提交成功")
            }
            false
        }
    }
}