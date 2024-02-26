package com.rouxinpai.arms.message.model

import org.greenrobot.eventbus.EventBus

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/29 21:55
 * desc   : [com.rouxinpai.arms.message.BaseWebSocketService]中订阅[ClientMessageEvent]来接收Activity或Fragment发送的消息。
 */
data class ClientMessageEvent(val message: String) {

    companion object {

        /**
         * 发送消息
         */
        fun postMessage(message: String) {
            EventBus.getDefault().post(ClientMessageEvent(message))
        }
    }
}