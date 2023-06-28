package com.rouxinpai.demo

import android.content.Context
import com.igexin.sdk.GTIntentService
import com.igexin.sdk.PushManager
import com.igexin.sdk.message.GTCmdMessage
import com.igexin.sdk.message.GTNotificationMessage
import com.igexin.sdk.message.GTTransmitMessage

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/28 15:34
 * desc   :
 */
class IntentService : GTIntentService() {

    /**
     * 个推进程启动成功回调该函数。
     */
    override fun onReceiveServicePid(context: Context, pid: Int) {
        super.onReceiveServicePid(context, pid)
    }

    /**
     * 个推初始化成功回调该函数并返回 cid。
     */
    override fun onReceiveClientId(context: Context, clientid: String) {
        super.onReceiveClientId(context, clientid)
    }

    /**
     * 此方法用于接收和处理透传消息。
     * 透传消息个推只传递数据，不做任何处理，客户端接收到透传消息后需要自己去做后续动作处理，如通知栏展示、弹框等。
     * 如果开发者在客户端将透传消息创建了通知栏展示，建议将展示和点击回执上报给个推。
     */
    override fun onReceiveMessageData(context: Context, pushMessage: GTTransmitMessage) {
        super.onReceiveMessageData(context, pushMessage)
    }

    /**
     * cid 在线状态变化时回调该函数
     */
    override fun onReceiveOnlineState(context: Context, online: Boolean) {
        super.onReceiveOnlineState(context, online)
    }

    /**
     * 调用设置标签、绑定别名、解绑别名、自定义回执操作的结果返回
     * action结果值说明：
     *  10009：设置标签的结果回执
     *  10010：绑定别名的结果回执
     *  10011：解绑别名的结果回执
     *  10012: 查询标签的结果回执
     *  10006：自定义回执的结果回执
     */
    override fun onReceiveCommandResult(context: Context, cmdMessage: GTCmdMessage) {
        super.onReceiveCommandResult(context, cmdMessage)
    }

    /**
     * 通知到达，只有个推通道下发的通知会回调此方法
     */
    override fun onNotificationMessageArrived(
        context: Context,
        notificationMessage: GTNotificationMessage
    ) {
        super.onNotificationMessageArrived(context, notificationMessage)
    }

    /**
     *  通知点击，只有个推通道下发的通知会回调此方法
     */
    override fun onNotificationMessageClicked(
        context: Context,
        notificationMessage: GTNotificationMessage
    ) {
        super.onNotificationMessageClicked(context, notificationMessage)
    }

    /**
     * 检测用户设备是否开启通知权限
     */
    override fun areNotificationsEnabled(context: Context, enable: Boolean) {
        super.areNotificationsEnabled(context, enable)
        if (!enable) {
            PushManager.getInstance().openNotification(context)
        }
    }
}