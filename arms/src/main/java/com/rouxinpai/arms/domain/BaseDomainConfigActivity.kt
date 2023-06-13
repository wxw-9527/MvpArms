package com.rouxinpai.arms.domain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.databinding.BaseDomainConfigActivityBinding
import com.rouxinpai.arms.extension.shake
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 11:51
 * desc   :
 */
@AndroidEntryPoint
abstract class BaseDomainConfigActivity :
    BaseMvpActivity<BaseDomainConfigActivityBinding, BaseDomainConfigContract.View, BaseDomainConfigPresenter>(),
    BaseDomainConfigContract.View,
    OnClickListener {

    override fun onCreateViewBinding(inflater: LayoutInflater): BaseDomainConfigActivityBinding {
        return BaseDomainConfigActivityBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 绑定监听事件
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_submit -> {
                val invitationCode = binding.etInvitationCode.text?.toString()
                if (invitationCode.isNullOrEmpty()) {
                    showToast(
                        R.string.base_domain_config__input_invitation_code,
                        type = FancyToast.WARNING
                    )
                    binding.textInputLayout.shake()
                    return
                }
                presenter.getDomainConfiguration(invitationCode)
            }
        }
    }
}