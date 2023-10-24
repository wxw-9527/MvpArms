@file:Suppress("DEPRECATION")

package com.rouxinpai.arms.print

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.printer.sdk.PrinterConstants
import com.printer.sdk.PrinterInstance
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.base.adapter.BaseVbAdapter
import com.rouxinpai.arms.databinding.ConnectPortablePrinterActivityBinding
import com.rouxinpai.arms.databinding.ConnectPortablePrinterRecycleItemBinding
import com.rouxinpai.arms.print.model.PrinterStatusEnum
import com.rouxinpai.arms.print.model.PrinterStatusEnum.*
import permissions.dispatcher.ktx.constructPermissionsRequest
import timber.log.Timber
import kotlin.concurrent.thread

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/11 15:55
 * desc   : 连接便携式打印机
 */
class ConnectPortablePrinterActivity : BaseActivity<ConnectPortablePrinterActivityBinding>(),
    OnItemClickListener {

    companion object {

        /**
         * 启动[ConnectPortablePrinterActivity]页
         */
        @JvmStatic
        fun start(activity: AppCompatActivity, launcher: ActivityResultLauncher<Intent>) {
            // 组装需要的权限
            val permissions = arrayListOf<String>().apply {
                // 定位权限
                add(Manifest.permission.ACCESS_FINE_LOCATION)
                add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }.toTypedArray()
            // 请求权限
            activity.constructPermissionsRequest(
                // 需要申请的权限
                *permissions,
                // 解释为什么需要这些权限
                onShowRationale = { request -> request.proceed() },
                // 用户不授予权限，则调用该方法
                onPermissionDenied = {}, // TODO: 需增加提示
                // 用户选择“不再询问”时调用该方法
                onNeverAskAgain = {}, // TODO: 需增加提示
                // 获取权限后执行该方法
                requiresPermission = {
                    val starter = Intent(activity, ConnectPortablePrinterActivity::class.java)
                    launcher.launch(starter)
                }
            ).launch()
        }
    }

    // 蓝牙适配器实例
    private var mBluetoothAdapter: BluetoothAdapter? = null

    // 蓝牙设备列表适配器
    private val mBluetoothDeviceAdapter = BluetoothDeviceAdapter()

    // 打印机实例
    private var mPrinterInstance: PrinterInstance? = null

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 初始化
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        // 判断设备支付支持蓝牙功能
        val bluetoothAdapter = mBluetoothAdapter
        if (bluetoothAdapter == null) {
            showErrorTip(R.string.connect_portable_printer__bluetooth_not_supported)
            finish()
        } else {
            // 启动蓝牙
            if (!bluetoothAdapter.isEnabled) {
                Timber.d("1、当前设备未开启蓝牙，申请启动蓝牙")
                enableBluetooth()
            } else {
                Timber.d("2、已开启蓝牙")
                scanDevice()
            }
        }
        // 注册设备扫描广播
        val filter = IntentFilter().apply {
            // 设备扫描相关
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            // 设备配对相关
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            // 蓝牙断开连接相关
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }
        registerReceiver(mBluetoothReceiver, filter)
        // 绑定适配器
        binding.rvBluetoothDevice.adapter = mBluetoothDeviceAdapter
        // 绑定监听事件
        mBluetoothDeviceAdapter.setOnItemClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        //
        if (true == mBluetoothAdapter?.isDiscovering) {
            mBluetoothAdapter?.cancelDiscovery()
        }
        // 取消广播注册
        unregisterReceiver(mBluetoothReceiver)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val item = adapter.getItemOrNull(position) as? BluetoothDevice ?: return
        bondDevice(item)
    }

    // 启动蓝牙
    private fun enableBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        mBluetoothLauncher.launch(enableBtIntent)
    }

    //
    private val mBluetoothLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (RESULT_OK == it.resultCode) {
            Timber.d("2、启动蓝牙成功")
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            scanDevice()
        } else {
            Timber.d("2、拒绝启动蓝牙，重新申请")
            enableBluetooth()
        }
    }

    // 搜索设备
    private fun scanDevice() {
        Timber.d("3、开始搜索设备")
        mBluetoothAdapter?.startDiscovery()
    }

    //
    private val mBluetoothReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (context == null || intent == null) return
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE) ?: return
            when (intent.action) {
                // 搜索到蓝牙设备
                BluetoothDevice.ACTION_FOUND -> {
                    val deviceType = device.bluetoothClass?.majorDeviceClass
                    if ((deviceType == 1536 || deviceType == 7936) && device.type != 2) {
                        val list = mBluetoothDeviceAdapter.data
                        if (!list.contains(device)) {
                            mBluetoothDeviceAdapter.addData(device)
                        }
                    }
                }
                // 蓝牙设备配对
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    when (device.bondState) {
                        BluetoothDevice.BOND_BONDING -> Timber.d("8、打印机配对中...")
                        BluetoothDevice.BOND_BONDED -> {
                            Timber.d("9、打印机配对成功")
                            dismissProgress()
                            connectPrinter(device)
                        }
                        BluetoothDevice.BOND_NONE -> {
                            Timber.d("10、取消配对")
                            dismissProgress()
                            showWarningTip(R.string.connect_portable_printer__unpair)
                        }
                    }
                }
            }
        }
    }

    // 配对设备
    private fun bondDevice(device: BluetoothDevice) {
        if (BluetoothDevice.BOND_NONE == device.bondState) {
            Timber.d("7、设备无配对记录，开始配对")
            // 打开输入配对密码的输入框
            showProgress(R.string.connect_portable_printer__pairing)
            // 配对
            device.createBond()
        } else {
            // 连接打印机
            connectPrinter(device)
        }
    }

    // 连接打印机
    private fun connectPrinter(device: BluetoothDevice) {
        Timber.d("11、连接打印机")
        // 获取打印机实例
        mPrinterInstance = PrinterInstance.getPrinterInstance(device, mHandler)
        // 连接打印机
        showProgress(R.string.connect_portable_printer__connecting)
        thread {
            mPrinterInstance?.openConnection()
        }
    }

    //
    private val mHandler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                // 连接成功
                PrinterConstants.Connect.SUCCESS -> {
                    val status = mPrinterInstance?.currentStatus
                    val printerStatusEnum = PrinterStatusEnum.fromStatus(status)
                    if (NORMAL == printerStatusEnum) {
                        Timber.d("12、打印机连接成功")
                        // 提示用户连接成功
                        dismissProgress()
                        showSuccessTip(R.string.connect_portable_printer__connect_successful)
                        // 设置返回值
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        // 提示用户异常信息
                        dismissProgress()
                        showWarningTip(printerStatusEnum.errorMsgResId)
                        // 断开连接
                        mPrinterInstance?.closeConnection()
                        mPrinterInstance = null
                    }
                }
                // 断开连接
                PrinterConstants.Connect.CLOSED -> {
                    dismissProgress()
                    showSuccessTip(R.string.connect_portable_printer__disconnect_success)
                }
                // 其他
                else -> {
                    Timber.d("13、打印机连接失败")
                    dismissProgress()
                    showErrorTip(R.string.connect_portable_printer__connect_fail)
                }
            }
        }
    }

    /**
     * 蓝牙设备列表
     */
    private class BluetoothDeviceAdapter :
        BaseVbAdapter<ConnectPortablePrinterRecycleItemBinding, BluetoothDevice>() {

        override fun onBindView(
            binding: ConnectPortablePrinterRecycleItemBinding,
            position: Int,
            item: BluetoothDevice,
        ) {
            binding.tvDeviceName.text = item.name
            binding.tvAddress.text = item.address
            binding.tvPaired.isVisible = (BluetoothDevice.BOND_BONDED == item.bondState)
        }
    }
}