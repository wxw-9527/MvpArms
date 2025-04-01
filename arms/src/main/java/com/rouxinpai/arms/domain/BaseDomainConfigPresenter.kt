package com.rouxinpai.arms.domain

import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.domain.api.DomainApi
import com.rouxinpai.arms.domain.model.DomainConfigurationVO
import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.arms.extension.urlEncode
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 11:51
 * desc   :
 */
class BaseDomainConfigPresenter @Inject constructor() :
    BasePresenter<BaseDomainConfigContract.View>(),
    BaseDomainConfigContract.Presenter {

    companion object {

        // 接口地址
        private const val URL_FORMAT = "%ssystem/customer/validCode/%s"
    }

    // 是否执行了onNext方法标识
    private var isExecutedOnNext = false

    override fun getDomainConfiguration(invitationCode: String) {
        view?.showProgress()

        val disposable = retrofit.create<DomainApi>()
            .getDomainConfiguration(
                String.format(URL_FORMAT, mGetDomainUrl, invitationCode.urlEncode())
            )
            .compose(responseTransformer())
            .map { data -> DomainConfigurationVO.fromDTO(data) }
            .compose(schedulersTransformer())
            .subscribeWith(object : DefaultObserver<DomainConfigurationVO>(view) {

                override fun onNext(t: DomainConfigurationVO) {
                    // 执行了onNext方法
                    isExecutedOnNext = true
                    // 缓存域名配置信息
                    DomainUtils.setDomainConfiguration(t)
                    // 跳转到登录页面
                    view?.dismissProgress()
                    view?.toLoginPage()
                }

                override fun onComplete() {
                    if (!isExecutedOnNext) {
                        view?.dismissProgress()
                        view?.showErrorTip(R.string.base_domain_config__invitation_code_invalid)
                    }
                }
            })
        addDisposable(disposable)
    }
}