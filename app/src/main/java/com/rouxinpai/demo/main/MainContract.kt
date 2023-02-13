package com.rouxinpai.demo.main

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 16:05
 * desc   :
 */
interface MainContract {

    interface View : IView

    interface Presenter : IPresenter<View> {
        fun print(text: String)
    }
}