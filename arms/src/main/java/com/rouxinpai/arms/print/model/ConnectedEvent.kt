package com.rouxinpai.arms.print.model

import org.greenrobot.eventbus.EventBus

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/26 17:53
 * desc   :
 */
class ConnectedEvent {

    companion object {

        fun post() {
            EventBus.getDefault().post(ConnectedEvent())
        }
    }
}