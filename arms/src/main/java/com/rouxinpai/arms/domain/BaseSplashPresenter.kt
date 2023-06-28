package com.rouxinpai.arms.domain

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.rouxinpai.arms.base.application.BaseApplication
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.arms.model.schedulersTransformer
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 11:18
 * desc   :
 */
class BaseSplashPresenter @Inject constructor(@ApplicationContext val context: Context) :
    BasePresenter<BaseSplashContract.View>(),
    BaseSplashContract.Presenter {

    companion object {

        // 事件标志
        private const val ACTION_TO_DOMAIN_CONFIG_PAGE = 0 // 跳转到域名配置页面
        private const val ACTION_TO_LOGIN_PAGE = 1 // 跳转到登录页面
        private const val ACTION_TO_MAIN_PAGE = 2 // 跳转到主页面
    }

    override fun onCreate(owner: LifecycleOwner) {
        val startMillis = System.currentTimeMillis()
        val disposable = Observable.create { emitter ->
            val domainConfiguration = DomainUtils.getDomainConfiguration()
            if (null == domainConfiguration) {
                emitter.onNext(ACTION_TO_DOMAIN_CONFIG_PAGE)
            } else {
                val accessToken = DomainUtils.getAccessToken()
                if (null == accessToken) {
                    emitter.onNext(ACTION_TO_LOGIN_PAGE)
                } else {
                    emitter.onNext(ACTION_TO_MAIN_PAGE)
                }
            }
        }
            .flatMap { action ->
                val delayTime = (context as BaseApplication).splashWaitDuration - (System.currentTimeMillis() - startMillis)
                Observable
                    .just(action)
                    .delay(delayTime, TimeUnit.MILLISECONDS)
            }
            .compose(schedulersTransformer())
            .subscribe { action ->
                when (action) {
                    ACTION_TO_DOMAIN_CONFIG_PAGE -> view?.toDomainConfigPage()
                    ACTION_TO_LOGIN_PAGE -> view?.toLoginPage()
                    ACTION_TO_MAIN_PAGE -> view?.toMainPage()
                }
            }
        addDisposable(disposable)
    }
}