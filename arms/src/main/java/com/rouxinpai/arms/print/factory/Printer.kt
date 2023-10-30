package com.rouxinpai.arms.print.factory

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.graphics.Bitmap
import com.rouxinpai.arms.print.model.TemplateVO

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/24 14:38
 * desc   :
 */
abstract class Printer {

    // 连接监听回调
    var onConnectListener: OnConnectListener? = null
        private set

    /**
     * 绑定监听回调
     */
    fun setOnConnectListener(listener: OnConnectListener?) {
        onConnectListener = listener
    }

    /**
     * 连接打印机
     */
    abstract fun connect(context: Context, device: BluetoothDevice)

    /**
     * 断开连接
     */
    abstract fun disconnect()

    /**
     * 是否已连接打印机
     */
    abstract fun isConnected(): Boolean

    /**
     * 释放资源
     */
    open fun destroy() {
        onConnectListener = null
    }

    abstract fun isStatusNormal(): Boolean

    abstract fun print(template: TemplateVO, bitmap: Bitmap): Boolean
}