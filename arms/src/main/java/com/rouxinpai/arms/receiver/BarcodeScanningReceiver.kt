package com.rouxinpai.arms.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/12/27 17:25
 * desc   :
 */
class BarcodeScanningReceiver: BroadcastReceiver() {

    companion object {
        // 广播动作
        const val ACTION = "com.android.scanner.broadcast"
        //
        private const val EXTRA = "scandata"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            return
        }
        if (context is OnScanListener) {
            val barcode = intent.getStringExtra(EXTRA)
            if (barcode != null && barcode.isNotBlank()) {
                context.onScanned(barcode)
            }
        }
    }

    interface OnScanListener {
        fun onScanned(value: String)
    }
}