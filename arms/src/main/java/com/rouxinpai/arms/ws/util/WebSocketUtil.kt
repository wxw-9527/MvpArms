package com.rouxinpai.arms.ws.util

import android.content.Context
import android.content.Intent
import android.os.Build
import com.rouxinpai.arms.ws.model.ClientMessageEvent
import com.rouxinpai.arms.ws.model.ServiceMessageEvent

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/29 22:37
 * desc   :
 */
object WebSocketUtil {

    //
    private var mClazz: Class<*>? = null

    /**
     * 初始化框架
     */
    fun init(clazz: Class<*>) {
        mClazz = clazz
    }

    /**
     * 启动服务
     */
    fun startService(context: Context) {
        // 启动服务
        Intent(context, mClazz).also { intent ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    /**
     * 销毁服务
     */
    fun stopService(context: Context) {
        context.stopService(Intent(context, mClazz))
    }

    /**
     * 发送消息给服务器
     */
    fun sendMessageToService(message: String) {
        ClientMessageEvent.postMessage(message)
    }

    /**
     * 发送消息给客户端
     */
    internal fun sendMessageToClient(json: String) {
        ServiceMessageEvent.postMessage(json)
    }
}