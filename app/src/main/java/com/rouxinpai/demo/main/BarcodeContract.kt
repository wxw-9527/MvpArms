package com.rouxinpai.demo.main

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/28 10:55
 * desc   :
 */
interface BarcodeContract {

    interface View : IView

    interface Presenter : IPresenter<View>
}