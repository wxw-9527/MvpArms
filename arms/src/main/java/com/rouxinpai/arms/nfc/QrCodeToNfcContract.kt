package com.rouxinpai.arms.nfc

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/8 10:11
 * desc   :
 */
sealed interface QrCodeToNfcContract {

    interface View : IView {

    }

    interface Presenter : IPresenter<View> {

    }
}