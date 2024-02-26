package com.rouxinpai.demo.feature.demo.notice

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.empty.EmptyContract
import com.rouxinpai.arms.empty.EmptyPresenter
import com.rouxinpai.arms.message.util.MessageUtil
import com.rouxinpai.demo.databinding.MessageDemoActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/22 17:22
 * desc   :
 */
@AndroidEntryPoint
class MessageDemoActivity :
    BaseMvpActivity<MessageDemoActivityBinding, EmptyContract.View, EmptyPresenter>(),
    EmptyContract.View, OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnConnect.setOnClickListener(this)
        binding.btnDisconnect.setOnClickListener(this)
        binding.btnSendMsg.setOnClickListener(this)
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
        MessageUtil.startService(this)
    }

    private fun onDisconnectClick() {
        // 销毁服务
        MessageUtil.stopService(this)
    }

    private fun onSendMsgClick() {
        // 发送消息
        MessageUtil.sendMessageToService("message")
    }
}