package com.rouxinpai.demo.feature.demo.rv

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.demo.model.entity.demo.rv.MaterialDTO

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/6 9:59
 * desc   :
 */
sealed interface RvLoadMoreContract {

    interface View : IView {
        fun showMaterialList(list: List<MaterialDTO>)
        fun showMoreMaterialList(list: List<MaterialDTO>)
    }

    interface Presenter : IPresenter<View> {
        fun listMaterials()
        fun listMoreMaterials()
    }
}