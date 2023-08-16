package com.rouxinpai.demo.feature.demo.upgrade

import android.os.Bundle
import android.view.LayoutInflater
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.update.model.ClientNameEnum
import com.rouxinpai.arms.update.model.ClientTypeEnum
import com.rouxinpai.demo.databinding.UpgradeActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/16 14:16
 * desc   :
 */
@AndroidEntryPoint
class UpgradeActivity :
    BaseMvpActivity<UpgradeActivityBinding, UpgradeContract.View, UpgradePresenter>(),
    UpgradeContract.View {

    override fun onCreateViewBinding(inflater: LayoutInflater): UpgradeActivityBinding {
        return UpgradeActivityBinding.inflate(inflater)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        binding.btnCheckVersion.setOnClickListener {
            presenter.getUpdateInfo(ClientTypeEnum.ANDROID, ClientNameEnum.DM, "arms")
        }
    }
}