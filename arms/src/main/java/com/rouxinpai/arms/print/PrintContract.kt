package com.rouxinpai.arms.print

import android.graphics.Bitmap
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.print.model.PrintResultVO
import com.rouxinpai.arms.print.model.TemplateVO

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/11 16:13
 * desc   :
 */
sealed interface PrintContract {

    interface View : IView {
        fun showBarcodeInfos(list: List<PrintResultVO>)
        fun showTemplates(templateList: List<TemplateVO>)
        fun sendPrintCommand(bitmap: Bitmap, index: Int)
    }

    interface Presenter : IPresenter<View> {
        fun listBarcodeInfos(barcodeList: List<String>)
        fun listTemplates()
        fun genImage(template: TemplateVO, barcodeInfo: BarcodeInfoVO, index: Int)
    }
}