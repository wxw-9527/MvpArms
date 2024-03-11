package com.rouxinpai.arms.print.factory

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.graphics.Bitmap
import com.printer.psdk.cpcl.CPCL
import com.printer.psdk.cpcl.GenericCPCL
import com.printer.psdk.cpcl.args.CImage
import com.printer.psdk.cpcl.args.CPage
import com.printer.psdk.cpcl.args.CPrint
import com.printer.psdk.device.adapter.ConnectedDevice
import com.printer.psdk.device.adapter.ReadOptions
import com.printer.psdk.device.bluetooth.Bluetooth
import com.printer.psdk.device.bluetooth.ConnectListener
import com.printer.psdk.device.bluetooth.Connection
import com.printer.psdk.frame.father.PSDK
import com.printer.psdk.imagep.android.AndroidSourceImage
import com.rouxinpai.arms.print.model.DirectionEnum
import com.rouxinpai.arms.print.model.TemplateVO
import timber.log.Timber
import java.io.IOException
import kotlin.concurrent.thread

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/7 9:47
 * desc   :
 */
class IBowlderPrinter : BasePrinter() {

    companion object {

        @Volatile
        private var instance: IBowlderPrinter? = null

        /**
         * 当前类单例对象
         */
        fun getInstance(): IBowlderPrinter =
            instance ?: synchronized(this) {
                instance ?: IBowlderPrinter().also { instance = it }
            }

        // 打印标志
        private val TAG = IBowlderPrinter::class.java.simpleName
    }

    //
    private var mConnection: Connection? = null

    //
    private var mGenericCPCL: GenericCPCL? = null

    override fun connect(context: Context, device: BluetoothDevice) {
        //
        mConnection = Bluetooth.getInstance().createConnectionClassic(device, mConnectListener)
        //
        if (mConnection == null) {
            onConnectListener?.onConnectFail()
            return
        }
        //
        thread {
            mConnection?.connect(null)
        }
    }

    //
    private val mConnectListener = object : ConnectListener {

        override fun onConnectSuccess(connectedDevice: ConnectedDevice?) {
            mGenericCPCL = CPCL.generic(connectedDevice)
        }

        override fun onConnectFail(errMsg: String?, e: Throwable?) {
            onConnectListener?.onConnectFail()
        }

        override fun onConnectionStateChanged(device: BluetoothDevice, state: Int) {
            when (state) {
                // 连接中
                Connection.STATE_CONNECTING -> {
                    Timber.tag(TAG).d("连接中")
                    onConnectListener?.onConnectStart()
                }
                // 配对中
                Connection.STATE_PAIRING -> Timber.tag(TAG).d("配对中")
                // 配对成功
                Connection.STATE_PAIRED -> Timber.tag(TAG).d("配对成功")
                // 连接成功
                Connection.STATE_CONNECTED -> {
                    Timber.tag(TAG).d("连接成功")
                    onConnectListener?.onConnectSuccessful(device)
                }
                // 连接断开
                Connection.STATE_DISCONNECTED -> {
                    Timber.tag(TAG).d("连接断开")
                    onConnectListener?.onConnectClosed()
                }
                // 连接已销毁
                Connection.STATE_RELEASED -> Timber.tag(TAG).d("连接已销毁")
            }
        }
    }

    override fun disconnect() {
        mConnection?.disconnect()
    }

    override fun isConnected(): Boolean {
        return try {
            mConnection?.isConnected ?: false // 调用disconnect()方法后，ConnectionImplClassic类中会把socketConnection对象置空，导致调用isConnected方法时报NullPointerException异常
        } catch (e: NullPointerException) {
            false
        }
    }

    override fun isStatusNormal(): Boolean {
        return Connection.STATE_CONNECTED == mConnection?.state
    }

    override fun print(template: TemplateVO, direction: DirectionEnum, bitmap: Bitmap): Boolean {
        val genericCPCL = mGenericCPCL ?: return false
        val (width, height) = if (DirectionEnum.HORIZONTAL == direction) {
            Pair(template.mediaSizeWidth, template.mediaSizeHeight)
        } else {
            Pair(template.mediaSizeHeight, template.mediaSizeWidth)
        }
        val page = CPage.builder()
            .width(width.toInt() * 8)
            .height(height.toInt() * 8)
            .build()
        val image = CImage.builder<AndroidSourceImage>()
            .startX(0)
            .startY(0)
            .image(AndroidSourceImage(bitmap))
            .compress(true)
            .build()
        val print = CPrint.builder().build()
        val gcpcl = genericCPCL
            .page(page)
            .image(image)
            .form()
            .print(print)
        val result = safeWriteAndRead(gcpcl)
        return result != null
    }

    private fun safeWriteAndRead(psdk: PSDK<*, *>): String? {
        return try {
            val reporter = psdk.write()
            if (!reporter.isOk) {
                throw IOException("写入数据失败", reporter.exception)
            }
            Thread.sleep(200)
            val bytes = psdk.read(ReadOptions.builder().timeout(6000).build())
            String(bytes)
        } catch (e: Exception) {
            null
        }
    }
}