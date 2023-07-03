package com.rouxinpai.arms.ws.model

import org.greenrobot.eventbus.EventBus

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/29 21:55
 * desc   : 在Service中可以使用[ServiceMessageEvent]作为事件对象，将要传递的数据包装在其中，并通过EventBus将其发布出去。
 */
data class ServiceMessageEvent(val json: String) {

    companion object {

        /**
         * 发送消息
         */
        fun postMessage(json: String) {
            EventBus.getDefault().post(ServiceMessageEvent(json))
        }
    }
}