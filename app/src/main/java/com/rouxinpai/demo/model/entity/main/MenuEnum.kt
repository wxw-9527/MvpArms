package com.rouxinpai.demo.model.entity.main

import com.rouxinpai.demo.feature.demo.dialog.DialogActivity
import com.rouxinpai.demo.feature.demo.switchdomain.SwitchDomainActivity

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/7/17 10:13
 * desc   :
 */
enum class MenuEnum(val value: String, val clazz: Class<*>) {

    /**
     * 切换域名
     */
    SWITCH_DOMAIN(value = "切换域名", SwitchDomainActivity::class.java),

    /**
     * 对话框
     */
    DIALOG(value = "对话框", clazz = DialogActivity::class.java),
}