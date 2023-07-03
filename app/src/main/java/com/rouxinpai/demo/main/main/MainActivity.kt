package com.rouxinpai.demo.main.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rouxinpai.arms.annotation.BarcodeScanningReceiverEnabled
import com.rouxinpai.arms.barcode.event.BarcodeEvent
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.base.adapter.BaseSimpleFragmentStateAdapter
import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.arms.ws.util.WebSocketUtil
import com.rouxinpai.demo.Application
import com.rouxinpai.demo.databinding.ActivityMainBinding
import com.rouxinpai.demo.main.BarcodeFragment
import com.rouxinpai.demo.main.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
@BarcodeScanningReceiverEnabled
class MainActivity : BaseMvpActivity<ActivityMainBinding, MainContract.View, MainPresenter>(),
    MainContract.View {

    companion object {

        /**
         * 启动[MainActivity]页
         */
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onParseData(intent: Intent) {
        super.onParseData(intent)
        Timber.d("------------------------> Activity收到数据 ${intent.getStringExtra("title")}")
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 启动服务
        WebSocketUtil.startService(this)
        // 绑定适配器
        binding.viewPager.adapter = PagerAdapter(this)
        binding.btnChangeDomain.setOnClickListener {
            // 停止服务
            WebSocketUtil.stopService(this)
            // 清除全部数据
            DomainUtils.clearAll()
            // 重启应用
            Application.instance.finishAllActivities()
            SplashActivity.start(this)
        }
        binding.btnScan.setOnClickListener {
            // presenter.getBarcodeInfo("1234567890")
            val msg = "{\"functionName\":\"/outboundOrderDetailRecordDraft/getMsg\",\"modelName\":\"cloud-wms-service\",\"params\":{\"outboundOrderId\":\"1672680836138090498\",\"materialCode\":\"s2\"}}"
            WebSocketUtil.sendMessageToService(msg)
        }
    }

    override fun onBarcodeEvent(event: BarcodeEvent) {
        super.onBarcodeEvent(event)
        Timber.d("------> Activity收到条码数据")
    }

    override fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent?): Boolean {
        return super.onKeyMultiple(keyCode, repeatCount, event)
    }

    private class PagerAdapter(activity: AppCompatActivity) :
        BaseSimpleFragmentStateAdapter(activity) {

        override fun getItemCount(): Int {
            return 1
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> BarcodeFragment.newInstance()
                else -> throw IndexOutOfBoundsException()
            }
        }
    }
}