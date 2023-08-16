package com.rouxinpai.demo.feature.demo.upgrade

import com.rouxinpai.arms.base.presenter.BasePresenter
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/16 14:17
 * desc   :
 */
class UpgradePresenter @Inject constructor() : BasePresenter<UpgradeContract.View>(),
    UpgradeContract.Presenter