package com.rouxinpai.arms.print

import android.graphics.Bitmap
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.print.model.DirectionEnum
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
        fun sendPrintCommand(template: TemplateVO, base64List: List<String>, copies: Int, direction: DirectionEnum)
    }

    interface Presenter : IPresenter<View> {
        fun getTemplate(): TemplateVO?
        fun listBarcodeInfos(barcodeList: List<String>)
        fun genImages(template: TemplateVO, barcodeInfoList: List<BarcodeInfoVO>, copies: Int, direction: DirectionEnum)
        fun base64ToBitmap(base64: String, newWidth: Double, direction: DirectionEnum): Bitmap
    }
}