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
import com.rouxinpai.arms.empty.EmptyContract
import com.rouxinpai.arms.empty.EmptyPresenter
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
    BaseMvpActivity<QrCodeToNfcActivityBinding, EmptyContract.View, EmptyPresenter>(),
    EmptyContract.View,
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
            val writeSuccessful = tryWriteNfcTag(intent, pendingText)
            if (writeSuccessful) {
                // 如果标签写入成功
                handleWriteSuccess()
            } else {
                // 如果标签写入失败
                handleWriteFailure()
            }
            return
        }
        // 如果标签写入失败或者没有待写入文本
        super.onParseNfcIntent(intent)
    }

    /**
     * 尝试写入NFC标签
     * @param intent Intent? NFC扫描到的Intent
     * @param text String 待写入文本
     * @return Boolean 是否写入成功
     */
    private fun tryWriteNfcTag(intent: Intent?, text: String): Boolean {
        val textToWrite = formatTextForNfc(text)
        return NfcUtil.writeTag(intent, textToWrite)
    }

    /**
     * 格式化待写入文本
     * @param text String 待写入文本
     * @return String 格式化后的文本
     */
    private fun formatTextForNfc(text: String): String {
        // 确保文本长度为2位，不足前面补0
        val length = text.length.toString().padStart(NfcUtil.FLAG_LENGTH, '0')
        return "$length$text"
    }

    /**
     * 处理写入成功
     */
    private fun handleWriteSuccess() {
        // 触发取消按钮点击事件重置页面状态
        binding.btnCancel.performClick()
        // 设置成功状态
        setResult(RESULT_OK)
        // 提示写入成功
        showSuccessTip(R.string.qr_code_to_nfc__write_successful)
    }

    /**
     * 处理写入失败
     */
    private fun handleWriteFailure() {
        // 提示写入失败
        showErrorTip(R.string.qr_code_to_nfc__write_fail)
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