package com.rouxinpai.demo.main

import com.rouxinpai.arms.base.presenter.BasePresenter
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/28 10:56
 * desc   :
 */
class BarcodePresenter @Inject constructor() : BasePresenter<BarcodeContract.View>(),
    BarcodeContract.Presenter {
}