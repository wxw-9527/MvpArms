package com.rouxinpai.demo.feature.demo.notice

import android.content.Context
import android.content.Intent
import com.rouxinpai.arms.message.BaseWebSocketService
import com.rouxinpai.demo.feature.demo.print.PrintDemoActivity
import com.rouxinpai.demo.feature.demo.switchdomain.SwitchDomainActivity
import com.rouxinpai.demo.feature.main.MainActivity

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/21 13:35
 * desc   :
 */
class WebSocketService : BaseWebSocketService() {

    override fun createIntents(context: Context): Array<Intent> {
        return arrayOf(
            Intent(context, MainActivity::class.java),
            Intent(context, SwitchDomainActivity::class.java),
            Intent(context, PrintDemoActivity::class.java),
        )
    }
}