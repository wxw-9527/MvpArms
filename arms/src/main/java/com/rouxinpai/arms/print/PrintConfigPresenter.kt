package com.rouxinpai.arms.print

import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.arms.print.api.PrintApi
import com.rouxinpai.arms.print.model.TemplateVO
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/25 14:48
 * desc   :
 */
class PrintConfigPresenter @Inject constructor() : BasePresenter<PrintConfigContract.View>(),
    PrintConfigContract.Presenter {

    override fun listTemplates() {
        view?.showProgress()
        val disposable = retrofit.create<PrintApi>()
            .listTemplates()
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .map { list -> list.map { dto -> TemplateVO.fromDto(dto) } }
            .subscribeWith(object : DefaultObserver<List<TemplateVO>>(view, false) {

                override fun onData(t: List<TemplateVO>) {
                    super.onData(t)
                    if (t.isEmpty()) {
                        view?.dismissProgress()
                        view?.showWarningTip(R.string.print__template_not_found)
                        return
                    }
                    view?.dismissProgress()
                    view?.showTemplates(t)
                }
            })
        addDisposable(disposable)
    }
}