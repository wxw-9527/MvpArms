package com.rouxinpai.demo.feature.demo.barcode

import android.app.Activity
import com.kongzue.dialogx.dialogs.BottomDialog
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.base.dialog.BaseMvpOnBindView
import com.rouxinpai.demo.R
import com.rouxinpai.demo.databinding.BarcodeDialogBinding
import com.rouxinpai.demo.di.DialogPresenterEntryPoint
import dagger.hilt.android.EntryPointAccessors

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/9 11:30
 * desc   :
 */
class BarcodeDialog :
    BaseMvpOnBindView<BottomDialog, BarcodeDialogBinding, BarcodeContract.View, BarcodePresenter>(R.layout.barcode_dialog), BarcodeContract.View {

    companion object {

        fun show() {
            val view = BarcodeDialog()
            BottomDialog
                .build()
                .setCancelable(false)
                .setTitle("标题")
                .setMessage("请扫描标签")
                .setCancelButton("取消")
                .setCustomView(view)
                .show()
        }
    }

    override fun onCreatePresenter(activity: Activity): BarcodePresenter {
        val accessors = EntryPointAccessors.fromActivity<DialogPresenterEntryPoint>(activity)
        return accessors.barcodePresenter()
    }

    override fun showBarcodeInfo(barcodeInfo: BarcodeInfoVO) {
        dismissProgress()
    }
}