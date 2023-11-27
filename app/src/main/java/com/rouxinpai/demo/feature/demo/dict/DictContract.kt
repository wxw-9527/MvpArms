package com.rouxinpai.demo.feature.demo.dict

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/24 17:30
 * desc   :
 */
sealed interface DictContract {

    interface View: IView

    interface Presenter: IPresenter<View>
}