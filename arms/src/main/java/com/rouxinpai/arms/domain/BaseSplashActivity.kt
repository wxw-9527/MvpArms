package com.rouxinpai.arms.domain

import android.annotation.SuppressLint
import android.view.LayoutInflater
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.databinding.BaseSplashActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 11:17
 * desc   :
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
abstract class BaseSplashActivity :
    BaseMvpActivity<BaseSplashActivityBinding, BaseSplashContract.View, BaseSplashPresenter>(),
    BaseSplashContract.View {

    override fun onCreateViewBinding(inflater: LayoutInflater): BaseSplashActivityBinding {
        return BaseSplashActivityBinding.inflate(inflater)
    }
}