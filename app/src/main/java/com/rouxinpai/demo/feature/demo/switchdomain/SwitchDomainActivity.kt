package com.rouxinpai.demo.feature.demo.switchdomain

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.kongzue.dialogx.dialogs.BottomDialog
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
        BottomDialog.show(
            R.string.switch_domain,
            R.string.switch_domain__dialog_message
        )
            .setCancelButton(R.string.switch_domain__cancel) { _, _ ->  false}
            .setOkButton(R.string.switch_domain__ok) { _, _ ->
                // 清除所有域名信息
                DomainUtils.clearAll()
                // 重启应用
                SplashActivity.start(this)
                Application.instance.finishAllActivities()
                //
                false
            }
    }
}