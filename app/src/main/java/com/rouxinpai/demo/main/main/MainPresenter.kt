package com.rouxinpai.demo.main.main

import androidx.lifecycle.LifecycleOwner
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.update.model.ClientNameEnum
import com.rouxinpai.arms.update.model.ClientTypeEnum
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 16:05
 * desc   :
 */
class MainPresenter @Inject constructor() : BasePresenter<MainContract.View>(),
    MainContract.Presenter {

    override fun onCreate(owner: LifecycleOwner) {
        getUpdateInfo(ClientTypeEnum.ANDROID, ClientNameEnum.QC, channel = "TestChannel")
    }

    override fun print(text: String) {
        getBarcodeInfo("1234567890")
    }
}