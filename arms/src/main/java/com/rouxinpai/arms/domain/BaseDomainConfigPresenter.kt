package com.rouxinpai.arms.domain

import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.di.qualifier.GetDomainConfigurationUrl
import com.rouxinpai.arms.domain.api.DomainApi
import com.rouxinpai.arms.domain.model.DomainConfigurationVO
import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.shashank.sony.fancytoastlib.FancyToast
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

    @Inject
    @GetDomainConfigurationUrl
    lateinit var getDomainConfigurationUrl: String

    override fun getDomainConfiguration(invitationCode: String) {
        view?.showProgress()
        val disposable = retrofit.create<DomainApi>()
            .getDomainConfiguration(getDomainConfigurationUrl + invitationCode)
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .map { data -> DomainConfigurationVO.fromDTO(data) }
            .subscribeWith(object : DefaultObserver<DomainConfigurationVO>(view) {

                override fun onData(t: DomainConfigurationVO) {
                    super.onData(t)
                    // 缓存域名配置信息
                    DomainUtils.setDomainConfiguration(t)
                    // 跳转到登录页面
                    view?.dismissProgress()
                    view?.toLoginPage()
                }

                override fun onEmpty() {
                    super.onEmpty()
                    view?.dismissProgress()
                    view?.showToast(
                        R.string.base_domain_config__invitation_code_invalid,
                        type = FancyToast.ERROR
                    )
                }
            })
        addDisposable(disposable)
    }
}