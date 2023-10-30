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
        fun sendPrintCommand(template: TemplateVO, bitmap: Bitmap, copies: Int, index: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getTemplate(): TemplateVO?
        fun listBarcodeInfos(barcodeList: List<String>)
        fun genImage(template: TemplateVO, barcodeInfo: BarcodeInfoVO, copies: Int, index: Int)
    }
}