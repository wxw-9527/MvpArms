package com.rouxinpai.arms.message.model

import org.greenrobot.eventbus.EventBus

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/26 16:14
 * desc   :
 */
class UnreadCountRefreshEvent {

    companion object {

        /**
         *
         */
        fun post() {
            EventBus.getDefault().post(UnreadCountRefreshEvent())
        }
    }
}