package com.rouxinpai.arms.print

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.printer.sdk.PrinterConstants
import com.printer.sdk.PrinterInstance
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.base.adapter.BaseVbAdapter
import com.rouxinpai.arms.databinding.PrintActivityBinding
import com.rouxinpai.arms.databinding.PrintRecycleItemBinding
import com.rouxinpai.arms.print.model.PrintResultVO
import com.rouxinpai.arms.print.model.PrinterStatusEnum
import com.rouxinpai.arms.print.model.PrintingStatusEnum
import com.rouxinpai.arms.print.model.TemplateVO
import com.rouxinpai.arms.view.OffsetDecoration
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
            .size(1, TypedValue.COMPLEX_UNIT_PX)
            .colorRes(com.kongzue.dialogx.R.color.black30)
            .showLastDivider()
            .build()
            .addTo(binding.rvPrintData)
        val bottomOffsetDecoration = OffsetDecoration(100f, TypedValue.COMPLEX_UNIT_DIP)
        binding.rvPrintData.addItemDecoration(bottomOffsetDecoration)
        // 获取条码数据
        presenter.listBarcodeInfos(mBarcodeList)
        // 绑定监听事件
        binding.btnPrint.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.print_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (R.id.action_disconnect == id) {
            onDisconnectClick()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // 断开连接
    private fun onDisconnectClick() {
        // 断开连接
        mPrinterInstance?.closeConnection()
        mPrinterInstance = null
        // 刷新按钮状态
        refreshButtonStatus()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_disconnect)?.isVisible = (mPrinterInstance != null && 0 == mPrinterInstance?.currentStatus)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun showBarcodeInfos(list: List<PrintResultVO>) {
        // 展示待打印条码信息
        mPrintDataAdapter.setList(list)
    }

    override fun showTemplates(templateList: List<TemplateVO>) {
        // 展示打印配置弹窗
        PrintConfigDialog.show(templateList) { template, copies ->
            // 展示进度条
            showProgress(R.string.print__printing)
            // 重置页码
            mIndex = 0
            // 生成图片
            presenter.genImage(template, mPrintDataAdapter.getItem(mIndex).barcodeInfo, copies, mIndex)
        }
    }

    override fun sendPrintCommand(template: TemplateVO, bitmap: Bitmap, copies: Int, index: Int) {
        thread {
            for (i in 1..copies) {
                // 判断打印机状态
                val printerStatusEnum = PrinterStatusEnum.fromStatus(mPrinterInstance?.currentStatus)
                // 打印机状态异常
                if (PrinterStatusEnum.NORMAL != printerStatusEnum) {
                    runOnUiThread {
                        dismissProgress()
                        showWarningTip(printerStatusEnum.errorMsgResId)
                    }
                    break
                }
                // 获取打印结果
                val printingStatusEnum = printAndReturnResult(template, bitmap)
                // 打印成功
                if (PrintingStatusEnum.COMPLETED == printingStatusEnum) {
                    // 最后一份打印完
                    if (i == copies) {
                        // 记录打印成功结果
                        runOnUiThread {
                            mPrintDataAdapter.getItem(index).printSuccess = true
                            mPrintDataAdapter.notifyItemChanged(index)
                        }
                        // 变更下标
                        mIndex = (index + 1)
                        // 打印下一条码
                        if (mIndex < mPrintDataAdapter.itemCount) {
                            presenter.genImage(template, mPrintDataAdapter.getItem(mIndex).barcodeInfo, copies, mIndex)
                        }
                        // 全部打印完成
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
                }
                // 打印失败
                else {
                    runOnUiThread {
                        dismissProgress()
                        showWarningTip(printingStatusEnum.errorMsgResId)
                    }
                }
            }
        }
    }

    // 打印并返回打印结果，耗时操作，需放在子线程中执行
    private fun printAndReturnResult(template: TemplateVO, bitmap: Bitmap): PrintingStatusEnum {
        // 设置打印参数
        val pageWith = template.printWith.toInt()
        val pageHeight = template.printHeight.toInt()
        mPrinterInstance?.pageSetup(PrinterConstants.LablePaperType.Size_58mm, pageWith, pageHeight)
        val offset = template.offset
        mPrinterInstance?.drawGraphic(offset, 0, bitmap)
        mPrinterInstance?.print(PrinterConstants.PRotate.Rotate_0, 1)
        // 返回打印结果
        return PrintingStatusEnum.fromStatus(mPrinterInstance?.getPrintingStatus(12 * 1000))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnPrint.id -> onPrintClick()
        }
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
        val printerStatusEnum = PrinterStatusEnum.fromStatus(printerInstance.currentStatus)
        if (PrinterStatusEnum.NORMAL != printerStatusEnum) {
            showErrorTip(printerStatusEnum.errorMsgResId)
            ConnectPortablePrinterActivity.start(this, mConnectLauncher)
            return
        }
        // 获取打印模板列表
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
        // 刷新菜单按钮状态
        invalidateOptionsMenu()
        // 打印机未连接
        if (printerInstance == null || 0 != printerInstance.currentStatus) {
            binding.btnPrint.setText(R.string.print__connect_printer_and_print)
        } else {
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