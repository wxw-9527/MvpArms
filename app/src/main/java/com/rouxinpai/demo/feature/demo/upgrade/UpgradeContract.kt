package com.rouxinpai.demo.feature.demo.upgrade

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/16 14:17
 * desc   :
 */
sealed interface UpgradeContract {

    interface View : IView

    interface Presenter : IPresenter<View>
}