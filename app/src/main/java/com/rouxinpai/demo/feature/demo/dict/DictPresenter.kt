package com.rouxinpai.demo.feature.demo.dict

import androidx.lifecycle.LifecycleOwner
import com.rouxinpai.arms.base.presenter.BasePresenter
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/24 17:31
 * desc   :
 */
class DictPresenter @Inject constructor() : BasePresenter<DictContract.View>(),
    DictContract.Presenter {

    override fun onCreate(owner: LifecycleOwner) {
        listDictItems()
    }
}