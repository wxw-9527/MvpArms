package com.rouxinpai.arms.empty

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/12/13 15:24
 * desc   :
 */
sealed interface EmptyContract {

    interface View: IView

    interface Presenter: IPresenter<View>
}