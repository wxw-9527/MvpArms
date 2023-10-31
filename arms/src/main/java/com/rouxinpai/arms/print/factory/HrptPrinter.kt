package com.rouxinpai.arms.print.factory

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.graphics.Bitmap
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.arms.print.model.TemplateVO
import cpcl.PrinterHelper
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/24 14:53
 * desc   :
 */
class HrptPrinter : BasePrinter() {

    companion object {

        @Volatile
        private var instance: HrptPrinter? = null

        /**
         * 当前类单例对象
         */
        fun getInstance(): HrptPrinter =
            instance ?: synchronized(this) {
                instance ?: HrptPrinter().also { instance = it }
            }
    }

    //
    private var mDisposable: Disposable? = null

    override fun connect(context: Context, device: BluetoothDevice) {
        onConnectListener?.onConnectStart()
        // 连接打印机
        val observable = Observable.create { emitter ->
            val result = PrinterHelper.portOpenBT(context, device.address)
            emitter.onNext(result)
            emitter.onComplete()
        }
        mDisposable = observable
            .compose(schedulersTransformer())
            .subscribe { result ->
                when (result) {
                    // 连接成功
                    0 -> onConnectListener?.onConnectSuccessful(device)
                    else -> onConnectListener?.onConnectFail()
                }
            }
    }

    override fun disconnect() {
        // 断开连接
        if (isConnected()) {
            PrinterHelper.portClose()
        }
        // 释放资源
        mDisposable?.dispose()
    }

    override fun isConnected(): Boolean {
        return PrinterHelper.IsOpened()
    }

    override fun isStatusNormal(): Boolean {
        return 0 == PrinterHelper.getPrinterStatus()
    }

    override fun print(template: TemplateVO, bitmap: Bitmap): Boolean {
        return PrinterHelper.printBitmap(0, 0, 0, bitmap, 1, true, 1) > 0
    }
}