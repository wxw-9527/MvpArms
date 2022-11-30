package com.rouxinpai.demo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.postDelayed
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.demo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseMvpActivity<ActivityMainBinding, MainContract.View, MainPresenter>(),
    MainContract.View {

    override val stateLayout: View
        get() = binding.refreshLayout

    override fun onCreateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)

        //
        showLoadingPage()
        Handler(Looper.getMainLooper()).postDelayed(2 * 1000) {
            showLoadingPage(msg = "测试标题", descId = R.string.app_name)
            Handler(Looper.getMainLooper()).postDelayed(2 * 1000) {
                showEmptyPage()
                Handler(Looper.getMainLooper()).postDelayed(2 * 1000) {
                    showEmptyPage(msgId = R.string.app_name)
                    Handler(Looper.getMainLooper()).postDelayed(2 * 1000) {
                        showErrorPage()
                        Handler(Looper.getMainLooper()).postDelayed(2 * 1000) {
                            showErrorPage(msg = "测试异常标题", descId = R.string.app_name)
                            Handler(Looper.getMainLooper()).postDelayed(2 * 1000) {
                                showSuccessPage()
                            }
                        }
                    }
                }
            }
        }

    }

}