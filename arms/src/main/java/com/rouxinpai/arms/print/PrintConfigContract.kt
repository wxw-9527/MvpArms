package com.rouxinpai.arms.print

import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.print.model.TemplateVO

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/25 14:48
 * desc   :
 */
sealed interface PrintConfigContract {

    interface View : IView {
        fun showTemplates(templateList: List<TemplateVO>)
    }

    interface Presenter : IPresenter<View> {
        fun listTemplates()
    }
}