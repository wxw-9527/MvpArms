package com.rouxinpai.demo.main

import com.rouxinpai.arms.base.presenter.BasePresenter
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 16:05
 * desc   :
 */
class MainPresenter @Inject constructor() : BasePresenter<MainContract.View>(),
    MainContract.Presenter {

    override fun print(text: String) {

    }
}