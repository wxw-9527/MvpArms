package com.rouxinpai.arms.nfc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import com.rouxinpai.arms.R
import com.rouxinpai.arms.annotation.BarcodeScanningReceiverEnabled
import com.rouxinpai.arms.annotation.EventBusEnabled
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.databinding.QrCodeToNfcActivityBinding
import com.rouxinpai.arms.nfc.util.NfcUtil
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/8 10:10
 * desc   :
 */
@AndroidEntryPoint
@BarcodeScanningReceiverEnabled
@EventBusEnabled
class QrCodeToNfcActivity :
    BaseMvpActivity<QrCodeToNfcActivityBinding, QrCodeToNfcContract.View, QrCodeToNfcPresenter>(),
    QrCodeToNfcContract.View,
    OnClickListener {

    companion object {

        /**
         * 启动[QrCodeToNfcActivity]页
         */
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, QrCodeToNfcActivity::class.java)
            context.startActivity(starter)
        }

        /**
         * 启动[QrCodeToNfcActivity]页并返回结果
         */
        @JvmStatic
        fun start(context: Context, launcher: ActivityResultLauncher<Intent>) {
            val starter = Intent(context, QrCodeToNfcActivity::class.java)
            launcher.launch(starter)
        }
    }

    // 待写入文本
    private var mPendingText: String? = null

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 绑定监听事件
        binding.btnCancel.setOnClickListener(this)
    }

    override fun onParseNfcIntent(intent: Intent?) {
        // 待写入字符串不为空且写入弹窗展示时，执行写入流程
        val pendingText = mPendingText
        if (!pendingText.isNullOrEmpty()) {
            val writeSuccessful = NfcUtil.writeTag(intent, pendingText)
            if (writeSuccessful) {
                // 触发取消按钮点击事件重置页面状态
                binding.btnCancel.performClick()
                // 设置成功状态
                setResult(RESULT_OK)
                // 提示
                showSuccessTip(R.string.qr_code_to_nfc__write_successful)
            } else {
                showErrorTip(R.string.qr_code_to_nfc__write_fail)
            }
            return
        }
        super.onParseNfcIntent(intent)
    }

    override fun showBarcodeInfo(barcodeInfo: BarcodeInfoVO) {
        if (barcodeInfo.isMaterialBarcode) {
            // 待写入文本赋值
            mPendingText = barcodeInfo.barcode
            // 渲染界面数据
            binding.tvProductCode.text = barcodeInfo.material.code
            binding.tvProductName.text = barcodeInfo.material.name
            binding.tvProductSn.text = mPendingText
            // 重置页面到待写入状态
            binding.llytScanBarcodeGuide.isVisible = false
            binding.groupBindTag.isVisible = true
            dismissProgress() // 关闭进度弹窗
            return
        }
        showWarningTip(R.string.qr_code_to_nfc__incorrect_barcode_type)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnCancel.id -> onCancelClick()
        }
    }

    //
    private fun onCancelClick() {
        // 待写入文本清空
        mPendingText = null
        // 重置页面到待扫描状态
        binding.groupBindTag.isVisible = false
        binding.llytScanBarcodeGuide.isVisible = true
    }
}