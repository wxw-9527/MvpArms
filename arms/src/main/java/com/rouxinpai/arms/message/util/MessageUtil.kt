package com.rouxinpai.arms.message.util

import android.content.Context
import android.content.Intent
import android.os.Build
import com.rouxinpai.arms.message.model.ClientMessageEvent

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/29 22:37
 * desc   :
 */
object MessageUtil {

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
}