package com.rouxinpai.demo.feature.demo.switchdomain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.demo.global.Application
import com.rouxinpai.demo.R
import com.rouxinpai.demo.databinding.SwitchDomainActivityBinding
import com.rouxinpai.demo.feature.splash.SplashActivity

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/7/17 10:26
 * desc   :
 */
class SwitchDomainActivity : BaseActivity<SwitchDomainActivityBinding>(), OnClickListener {

    override fun onCreateViewBinding(inflater: LayoutInflater): SwitchDomainActivityBinding {
        return SwitchDomainActivityBinding.inflate(inflater)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 展示基本信息
        showDomainInfo()
        // 绑定监听事件
        binding.btnSwitchDomain.setOnClickListener(this)
    }

    // 展示基本信息
    private fun showDomainInfo() {
        val domainConfiguration = DomainUtils.getDomainConfiguration()
        if (domainConfiguration != null) {
            binding.tvText.text = buildString {
                appendLine(domainConfiguration.customerName)
                appendLine(domainConfiguration.domain)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_switch_domain -> onSwitchDomainClick()
        }
    }

    // 切换域名
    private fun onSwitchDomainClick() {
        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT))
            .title(R.string.switch_domain)
            .message(R.string.switch_domain__dialog_message)
            .negativeButton()
            .positiveButton {
                // 清除所有域名信息
                DomainUtils.clearAll()
                // 重启应用
                SplashActivity.start(this)
                Application.instance.finishAllActivities()
            }
            .show()
    }
}