package com.rouxinpai.arms.print

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.kongzue.dialogx.dialogs.BottomDialog
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener
import com.rouxinpai.arms.R
import com.rouxinpai.arms.annotation.EventBusEnabled
import com.rouxinpai.arms.base.dialog.BaseMvpOnBindView
import com.rouxinpai.arms.databinding.PrintConfigDialogBinding
import com.rouxinpai.arms.di.DialogPresenterEntryPoint
import com.rouxinpai.arms.print.factory.BasePrinter
import com.rouxinpai.arms.print.factory.PrinterFactory
import com.rouxinpai.arms.print.model.BrandEnum
import com.rouxinpai.arms.print.model.ConnectedEvent
import com.rouxinpai.arms.print.model.DirectionEnum
import com.rouxinpai.arms.print.model.TemplateVO
import com.rouxinpai.arms.print.util.PrintUtil
import com.skydoves.powerspinner.DefaultSpinnerAdapter
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import dagger.hilt.android.EntryPointAccessors
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/19 14:42
 * desc   :
 */
@EventBusEnabled
class PrintConfigDialog(
    private val activity: AppCompatActivity,
    private val success: (template: TemplateVO, copies: Int, direction: DirectionEnum) -> Unit,
) : BaseMvpOnBindView<BottomDialog, PrintConfigDialogBinding, PrintConfigContract.View, PrintConfigPresenter>(
    R.layout.print_config_dialog
), PrintConfigContract.View,
    OnClickListener {

    companion object {

        /**
         * 展示[PrintConfigDialog]弹窗
         */
        fun show(
            activity: AppCompatActivity,
            dismiss: () -> Unit,
            success: (template: TemplateVO, copies: Int, direction: DirectionEnum) -> Unit,
        ) {
            val view = PrintConfigDialog(activity, success)
            BottomDialog.build()
                .setDialogLifecycleCallback(object : DialogLifecycleCallback<BottomDialog>() {

                    override fun onDismiss(dialog: BottomDialog?) {
                        super.onDismiss(dialog)
                        dismiss.invoke()
                    }
                })
                .setTitle(R.string.print_config__title)
                .setCustomView(view)
                .show()
        }
    }

    // 模板相关
    private var mTemplateList: List<TemplateVO>? = null
    private var mTemplate: TemplateVO? = null

    // 打印机品牌相关
    private lateinit var mBrandEnumList: List<BrandEnum>
    private lateinit var mBrandEnum: BrandEnum

    // 打印方向
    private lateinit var mDirectionEnumList: List<DirectionEnum>
    private lateinit var mDirectionEnum: DirectionEnum

    // 打印机实例
    private lateinit var mPrinter: BasePrinter

    // 蓝牙设备对象
    private var mBluetoothDevice: BluetoothDevice? = null

    override fun onCreatePresenter(activity: Activity): PrintConfigPresenter {
        val accessors = EntryPointAccessors.fromActivity<DialogPresenterEntryPoint>(activity)
        return accessors.printConfigPresenter()
    }

    override fun onCreate(owner: LifecycleOwner) {
        // 对话框按钮初始化
        dialog
            .setCancelButton(R.string.print_config__cancel)
            .setOkButton(R.string.print_config__ok, mOkClickListener)
        super.onCreate(owner)
        // 全局变量初始化
        mBrandEnumList = BrandEnum.values().asList()
        mBrandEnum = PrintUtil.getBrandEnum()
        mDirectionEnumList = DirectionEnum.values().asList()
        mDirectionEnum = PrintUtil.getDirectionEnum()
        // 打印模板相关初始化
        with(binding.spinnerTemplate) {
            setSpinnerAdapter(DefaultSpinnerAdapter(this))
            setOnSpinnerOutsideTouchListener { _, _ -> dismiss() }
            setOnSpinnerItemSelectedListener(mOnTemplateItemSelectedListener)
        }
        // 打印机品牌相关初始化
        with(binding.spinnerBrand) {
            setSpinnerAdapter(DefaultSpinnerAdapter(this))
            setOnSpinnerOutsideTouchListener { _, _ -> dismiss() }
            setOnSpinnerItemSelectedListener(mOnBrandItemSelectedListener)
            setItems(mBrandEnumList.map { context.getString(it.brandNameResId) })
            selectItemByIndex(mBrandEnumList.indexOf(mBrandEnum))
        }
        // 打印机信息初始化
        showConnectedBluetoothDevice()
        // 步进器初始化
        with(binding.stepperCopies) {
            setMinValue(1f)
            setMaxValue(12f)
            setValue(1f)
        }
        // 打印方向初始化
        with(binding.spinnerDirection) {
            setSpinnerAdapter(DefaultSpinnerAdapter(this))
            setOnSpinnerOutsideTouchListener { _, _ -> dismiss() }
            setOnSpinnerItemSelectedListener(mOnDirectionItemSelectedListener)
            setItems(mDirectionEnumList.map { context.getString(it.textResId) })
            selectItemByIndex(mDirectionEnumList.indexOf(mDirectionEnum))
        }
        // 绑定监听事件
        binding.btnPrinter.setOnClickListener(this)
        binding.btnDisconnect.setOnClickListener(this)
        // 获取打印模板
        presenter.listTemplates()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onConnectedEvent(event: ConnectedEvent) {
        showConnectedBluetoothDevice()
    }

    override fun showTemplates(templateList: List<TemplateVO>) {
        // 初始化全局变量
        mTemplateList = templateList
        // 渲染下拉框数据
        binding.spinnerTemplate.setItems(templateList.map { it.name })
        // 设置默认选中
        mTemplate = PrintUtil.getTemplate()
        if (mTemplate != null) {
            val index = templateList.indexOf(mTemplate)
            binding.spinnerTemplate.selectItemByIndex(index)
        }
    }

    // 确认按钮点击事件
    private val mOkClickListener = OnDialogButtonClickListener<BottomDialog> { _, _ ->
        // 打印模板
        val template = mTemplate
        if (template == null) {
            showWarningTip(R.string.print_config__template_hint)
            return@OnDialogButtonClickListener true
        }
        // 连接打印机
        if (mBluetoothDevice == null || !mPrinter.isConnected()) {
            showWarningTip(R.string.print_config__printer_hint)
            return@OnDialogButtonClickListener true
        }
        // 份数
        val copies = binding.stepperCopies.getValue()?.toInt()
        if (copies == null) {
            showWarningTip(R.string.print_config__copies_hint)
            return@OnDialogButtonClickListener true
        }
        //
        success.invoke(template, copies, mDirectionEnum)
        false
    }

    // 打印模板选中监听
    private val mOnTemplateItemSelectedListener = OnSpinnerItemSelectedListener<String> { oldIndex, _, newIndex, _ ->
        if (newIndex != oldIndex) {
            // 设置选中
            mTemplate = mTemplateList?.get(newIndex)
            // 缓存选中模板
            mTemplate?.let { PrintUtil.setTemplate(it) }
        }
    }

    // 打印机品牌选中监听
    private val mOnBrandItemSelectedListener = OnSpinnerItemSelectedListener<String> { oldIndex, _, newIndex, _ ->
        if (newIndex != oldIndex) {
            // 设置选中
            mBrandEnum = mBrandEnumList[newIndex]
            // 初始化打印机工厂
            mPrinter = PrinterFactory.createPrinter(mBrandEnum)
            // 初次不执行以下代码
            if (-1 != oldIndex) {
                // 缓存选中打印机品牌
                PrintUtil.setBrandEnum(mBrandEnum)
                // 断开打印机连接
                onDisconnectClick()
            }
        }
    }

    // 打印方向选中监听
    private val mOnDirectionItemSelectedListener = OnSpinnerItemSelectedListener<String> { oldIndex, _, newIndex, _ ->
        if (newIndex != oldIndex) {
            // 设置选中
            mDirectionEnum = mDirectionEnumList[newIndex]
            // 缓存选中打印方向
            PrintUtil.setDirectionEnum(mDirectionEnum)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnPrinter.id -> onConnectPrinter()
            binding.btnDisconnect.id -> onDisconnectClick()
        }
    }

    // 连接打印机
    private fun onConnectPrinter() {
        if (mBluetoothDevice == null || !mPrinter.isConnected()) {
            ConnectPortablePrinterActivity.start(activity)
        }
    }

    // 断开打印机连接
    private fun onDisconnectClick() {
        mPrinter.disconnect()
        showConnectedBluetoothDevice()
    }

    // 展示已连接打印机信息
    private fun showConnectedBluetoothDevice() {
        // 初始化蓝牙设备信息
        mBluetoothDevice = PrintUtil.getBluetoothDevice()
        if (mPrinter.isConnected() && mBluetoothDevice != null) {
            binding.btnPrinter.text = mBluetoothDevice?.name
            binding.btnDisconnect.isVisible = true
        } else {
            binding.btnPrinter.text = null
            binding.btnDisconnect.isVisible = false
        }
    }
}