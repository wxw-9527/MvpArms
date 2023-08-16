package com.rouxinpai.demo.model.entity.main

import com.rouxinpai.demo.feature.demo.balloon.BalloonActivity
import com.rouxinpai.demo.feature.demo.dialog.DialogActivity
import com.rouxinpai.demo.feature.demo.dropdown.DropdownActivity
import com.rouxinpai.demo.feature.demo.switchdomain.SwitchDomainActivity
import com.rouxinpai.demo.feature.demo.upgrade.UpgradeActivity

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

    /**
     * 气泡提示
     */
    BALLOON(value = "气泡提示", clazz = BalloonActivity::class.java),

    /**
     * 下拉框
     */
    DROPDOWN(value = "下拉框", clazz = DropdownActivity::class.java),

    /**
     * 版本更新
     */
    UPGRADE(value = "版本更新", clazz = UpgradeActivity::class.java)
}