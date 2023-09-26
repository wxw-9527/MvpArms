package com.rouxinpai.demo.feature.demo.message

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.ws.util.WebSocketUtil
import com.rouxinpai.demo.databinding.MessageDemoActivityBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/22 17:22
 * desc   :
 */
class MessageDemoActivity : BaseActivity<MessageDemoActivityBinding>(), OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnConnect.setOnClickListener(this)
        binding.btnDisconnect.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnConnect.id -> onConnectClick()
            binding.btnDisconnect.id -> onDisconnectClick()
            binding.btnSendMsg.id -> onSendMsgClick()
        }
    }

    private fun onConnectClick() {
        // 启动服务
        WebSocketUtil.startService(this)
    }

    private fun onDisconnectClick() {
        // 销毁服务
        WebSocketUtil.stopService(this)
    }

    private fun onSendMsgClick() {
        // 发送消息
        WebSocketUtil.sendMessageToService("message")
    }
}