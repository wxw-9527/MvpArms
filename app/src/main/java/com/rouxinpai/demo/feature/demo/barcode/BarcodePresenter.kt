package com.rouxinpai.demo.feature.demo.barcode

import com.rouxinpai.arms.barcode.model.BillType
import com.rouxinpai.arms.barcode.model.BillTypeEnum
import com.rouxinpai.arms.base.presenter.BasePresenter
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/9 10:21
 * desc   :
 */
@BillType(BillTypeEnum.SN, BillTypeEnum.BOM_CODE)
class BarcodePresenter @Inject constructor() : BasePresenter<BarcodeContract.View>(),
    BarcodeContract.Presenter