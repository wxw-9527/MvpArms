package com.rouxinpai.demo.feature.demo.filedownload

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import com.blankj.utilcode.util.AppUtils
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.util.DownloadUtil
import com.rouxinpai.demo.databinding.FileDownloadActivityBinding
import java.io.File

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/12/28 15:59
 * desc   :
 */
class FileDownloadActivity : BaseActivity<FileDownloadActivityBinding>(),
    DownloadUtil.OnDownloadListener {

    private val mSb = StringBuilder()

    private var mStartMillis = 0L

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 下载文件
        binding.btnDownload.setOnClickListener {
            DownloadUtil.getInstance().startDownload(
                binding.etUrl.text.toString(),
                "test.apk",
                this
            )
        }
    }

    override fun onDownloadStart() {
        mStartMillis = System.currentTimeMillis()
        mSb.clear()
        mSb.appendLine("======> 开始下载")
        binding.tvResult.text = mSb.toString()
        showProgress()
    }

    override fun onDownloading(percent: Float) {
        mSb.appendLine("======> 下载中，已下载${percent * 100}%")
        binding.tvResult.text = mSb.toString()
    }

    override fun onDownloadFail(e: Exception?) {
        mSb.appendLine("======> ${e?.message}")
        binding.tvResult.text = mSb.toString()
        dismissProgress()
    }

    override fun onDownloadComplete(file: File) {
        mSb.appendLine("======> 耗时：${(System.currentTimeMillis() - mStartMillis) / 1000L}秒，位置：${file.path}")
        binding.tvResult.text = mSb.toString()
        dismissProgress()
        Handler(Looper.getMainLooper()).postDelayed(1000L) {
            AppUtils.installApp(file)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DownloadUtil.getInstance().onDestroy()
    }
}