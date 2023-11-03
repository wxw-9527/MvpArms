package com.rouxinpai.arms.nfc

import com.rouxinpai.arms.base.presenter.BasePresenter
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/8 10:12
 * desc   :
 */
class QrCodeToNfcPresenter @Inject constructor() : BasePresenter<QrCodeToNfcContract.View>(),
    QrCodeToNfcContract.Presenter