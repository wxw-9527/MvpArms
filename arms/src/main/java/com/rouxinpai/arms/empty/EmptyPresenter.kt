package com.rouxinpai.arms.empty

import com.rouxinpai.arms.base.presenter.BasePresenter
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/12/13 15:24
 * desc   :
 */
class EmptyPresenter @Inject constructor() : BasePresenter<EmptyContract.View>(),
    EmptyContract.Presenter