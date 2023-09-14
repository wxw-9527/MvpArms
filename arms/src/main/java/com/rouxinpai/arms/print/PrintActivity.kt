package com.rouxinpai.arms.print

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.kongzue.dialogx.dialogs.BottomMenu
import com.printer.sdk.PrinterConstants
import com.printer.sdk.PrinterInstance
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.base.adapter.BaseVbAdapter
import com.rouxinpai.arms.databinding.PrintActivityBinding
import com.rouxinpai.arms.databinding.PrintRecycleItemBinding
import com.rouxinpai.arms.print.model.PrintResultVO
import com.rouxinpai.arms.print.model.TemplateVO
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.thread

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/11 16:13
 * desc   : 打印页
 */
@AndroidEntryPoint
class PrintActivity : BaseMvpActivity<PrintActivityBinding, PrintContract.View, PrintPresenter>(),
    PrintContract.View,
    OnClickListener {

    companion object {

        // 参数传递标志
        private const val ARG_BARCODE_LIST = "arg_barcode_list" // 条码列表

        /**
         * 启动[PrintActivity]页
         */
        @JvmStatic
        fun start(
            context: Context,
            launcher: ActivityResultLauncher<Intent>,
            barcodeList: List<String>,
        ) {
            val starter = Intent(context, PrintActivity::class.java).apply {
                val bundle = bundleOf(ARG_BARCODE_LIST to barcodeList)
                putExtras(bundle)
            }
            launcher.launch(starter)
        }

        /**
         * 启动[PrintActivity]页
         */
        @JvmStatic
        fun start(context: Context, barcodeList: List<String>) {
            val starter = Intent(context, PrintActivity::class.java).apply {
                val bundle = bundleOf(ARG_BARCODE_LIST to barcodeList)
                putExtras(bundle)
            }
            context.startActivity(starter)
        }
    }

    // 条码列表
    private lateinit var mBarcodeList: List<String>

    // 打印数据集列表适配器
    private val mPrintDataAdapter = PrintDataAdapter()

    // 打印机实例
    private var mPrinterInstance: PrinterInstance? = null

    // 选中的打印模板
    private lateinit var mSelectedTemplate: TemplateVO

    // 当前打印下标
    private var mIndex = 0

    override val stateLayout: View
        get() = binding.rvPrintData

    override fun onParseData(bundle: Bundle) {
        super.onParseData(bundle)
        mBarcodeList = bundle.getStringArrayList(ARG_BARCODE_LIST).orEmpty()
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        //
        refreshButtonStatus()
        // 绑定列表适配器
        binding.rvPrintData.adapter = mPrintDataAdapter
        // 添加分割线
        dividerBuilder()
            .asSpace()
            .size(1, TypedValue.COMPLEX_UNIT_PX)
            .colorRes(com.kongzue.dialogx.R.color.black30)
            .build()
            .addTo(binding.rvPrintData)
        // 获取条码数据
        presenter.listBarcodeInfos(mBarcodeList)
        // 绑定监听事件
        binding.btnDisconnect.setOnClickListener(this)
        binding.btnPrint.setOnClickListener(this)
    }

    override fun showBarcodeInfos(list: List<PrintResultVO>) {
        // 展示待打印条码信息
        mPrintDataAdapter.setList(list)
    }

    override fun showTemplates(templateList: List<TemplateVO>) {
        val itemTextList = templateList.map { it.name }
        BottomMenu
            .show(itemTextList)
            .setTitle(R.string.print__select_template)
            .setSelection(binding.btnPrint.tag as? Int ?: -1)
            .setOnMenuItemClickListener { _, _, index ->
                // 设置选中模板
                mSelectedTemplate = templateList[index]
                // 展示进度条
                showProgress(R.string.print__printing)
                // 生成图片
                mIndex = 0 // 重置页码
                presenter.genImage(mSelectedTemplate, mPrintDataAdapter.getItem(mIndex).barcodeInfo, mIndex)
                // 返回false自动关闭菜单
                false
            }

    }

    override fun sendPrintCommand(bitmap: Bitmap, index: Int) {
        thread {
            // 设置打印参数
            val pageWith = mSelectedTemplate.printWith.toInt()
            val pageHeight = mSelectedTemplate.printHeight.toInt()
            mPrinterInstance?.pageSetup(
                PrinterConstants.LablePaperType.Size_58mm,
                pageWith,
                pageHeight
            )
            val offset = mSelectedTemplate.offset
            mPrinterInstance?.drawGraphic(offset, 0, bitmap)
            mPrinterInstance?.print(PrinterConstants.PRotate.Rotate_0, 1)
            // 判断是否打印完成
            val ret = mPrinterInstance?.getPrintingStatus(12 * 1000)
            // 打印成功
            if (0 == ret) {
                // 记录打印成功结果
                runOnUiThread {
                    mPrintDataAdapter.getItem(index).printSuccess = true
                    mPrintDataAdapter.notifyItemChanged(index)
                }
                // 变更下标
                mIndex = (index + 1)
                // 打印下一条码
                if (mIndex < mPrintDataAdapter.itemCount) {
                    presenter.genImage(mSelectedTemplate, mPrintDataAdapter.getItem(mIndex).barcodeInfo, mIndex)
                }
                // 打印完成
                else {
                    // 设置返回状态
                    setResult(RESULT_OK)
                    // 展示提示信息
                    runOnUiThread {
                        dismissProgress()
                        showSuccessTip(R.string.print__print_successful)
                    }
                }
            }
            // 打印失败
            else {
                dismissProgress()
                runOnUiThread { showWarningTip(R.string.print__print_failed) }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnDisconnect.id -> onDisconnectClick()
            binding.btnPrint.id -> onPrintClick()
        }
    }

    // 断开连接
    private fun onDisconnectClick() {
        // 断开连接
        mPrinterInstance?.closeConnection()
        mPrinterInstance = null
        // 刷新按钮状态
        refreshButtonStatus()
    }

    // 打印
    private fun onPrintClick() {
        // 打印机实例
        val printerInstance = mPrinterInstance
        // 打印机未连接
        if (printerInstance == null) {
            ConnectPortablePrinterActivity.start(this, mConnectLauncher)
            return
        }
        // 打印机状态异常
        if (0 != printerInstance.currentStatus) {
            showErrorTip(R.string.print__printer_exception)
            ConnectPortablePrinterActivity.start(this, mConnectLauncher)
            return
        }
        presenter.listTemplates()
    }

    // 连接打印机回调
    private val mConnectLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (RESULT_OK == it.resultCode) {
            //
            refreshButtonStatus()
            // 继续执行打印方法
            binding.btnPrint.performClick()
        }
    }

    // 刷新按钮状态
    private fun refreshButtonStatus() {
        // 初始化打印机实例
        mPrinterInstance = PrinterInstance.mPrinter
        // 打印机实例
        val printerInstance = mPrinterInstance
        // 打印机未连接
        if (printerInstance == null || 0 != printerInstance.currentStatus) {
            binding.btnDisconnect.visibility = View.GONE
            binding.btnPrint.setText(R.string.print__connect_printer_and_print)
        } else {
            binding.btnDisconnect.visibility = View.VISIBLE
            binding.btnPrint.setText(R.string.print)
        }
    }

    /**
     * 条码列表
     */
    private class PrintDataAdapter :
        BaseVbAdapter<PrintRecycleItemBinding, PrintResultVO>() {

        override fun onBindView(
            binding: PrintRecycleItemBinding,
            position: Int,
            item: PrintResultVO,
        ) {
            // 条码信息
            val barcodeInfo = item.barcodeInfo
            // 物料条码
            if (barcodeInfo.isMaterialBarcode) {
                binding.tvCode.text = barcodeInfo.material.code
                binding.tvName.text = barcodeInfo.material.name
            }
            // 库位条码
            else if (barcodeInfo.isWarehouseLocationBarcode) {
                binding.tvCode.text = barcodeInfo.warehouse.code
                binding.tvName.text = barcodeInfo.warehouse.name
            }
            // 其他类型条码
            else {
                // TODO: 其他类型条码待实现
            }
            // 条码
            binding.tvBarcode.text = barcodeInfo.barcode
            // 状态
            binding.tvStatus.setText(if (item.printSuccess) R.string.print__printed else R.string.print__unprinted)
        }
    }
}