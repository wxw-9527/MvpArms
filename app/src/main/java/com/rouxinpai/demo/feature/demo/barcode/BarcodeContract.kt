package com.rouxinpai.demo.feature.demo.barcode

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/9 10:20
 * desc   :
 */
sealed interface BarcodeContract {

    interface View : IView

    interface Presenter : IPresenter<View>
}