package com.rouxinpai.arms.print

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.print.model.MaterialVO

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/4/1 10:24
 * desc   :
 */
sealed interface SelectBarcodeContract {

    interface View : IView {
        fun showMaterialList(list: List<MaterialVO>)
    }

    interface Presenter : IPresenter<View> {
        fun listBarcodeInfos(barcodeList: List<String>)
    }
}