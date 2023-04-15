package com.rouxinpai.arms.barcode.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rouxinpai.arms.barcode.event.BarcodeEvent

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/12/27 17:25
 * desc   :
 */
class BarcodeScanningReceiver : BroadcastReceiver() {

    companion object {
        // 广播动作
        val sActions = arrayOf("com.android.scanner.broadcast", "com.kte.scan.result")
        //
        private val sExtras = arrayOf("scandata", "code")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            return
        }
        sExtras.forEach { extra ->
            val barcode = intent.getStringExtra(extra)
            if (!barcode.isNullOrBlank()) {
                BarcodeEvent.post(barcode)
                return
            }
        }
    }
}