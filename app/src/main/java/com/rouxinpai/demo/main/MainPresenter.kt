package com.rouxinpai.demo.main

import androidx.lifecycle.LifecycleOwner
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.demo.R
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 16:05
 * desc   :
 */
class MainPresenter @Inject constructor() : BasePresenter<MainContract.View>(),
    MainContract.Presenter {

    override fun onCreate(owner: LifecycleOwner) {
        super<BasePresenter>.onCreate(owner)
        getUpdateInfo(clientName = "test", channel = "TestChannel")
    }

    override fun print(text: String) {
//        request<JsonObject> {
//            call {
//                retrofit.create<Api>().getCaptcha()
//            }
//            success { total, data ->
//                Timber.d("请求成功")
//                delay(2000L)
//                Timber.d("延时成功")
//            }
//        }
        launch {
            view?.showProgress(R.string.app_name, com.rouxinpai.calendarview.R.string.cv_app_name)
            delay(2000L)
            for (i in 1..6) {
                view?.updateProgress("正在打印第${i}份")
                delay(1000L)
            }
            view?.dismiss()
            view?.showToast("打印完成", type = FancyToast.SUCCESS)
        }
    }
}