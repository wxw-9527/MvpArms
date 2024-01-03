package com.rouxinpai.demo.feature.demo.filedownload

import android.os.Bundle
import com.blankj.utilcode.util.PathUtils
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
class FileDownloadActivity : BaseActivity<FileDownloadActivityBinding>(), DownloadUtil.OnDownloadListener {

    companion object {

        // 文件存储位置
        private val FILE_PATH: String = PathUtils.getExternalAppFilesPath()
    }

    private val mSb = StringBuilder()

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 初始化 Aria
        DownloadUtil.getInstance().register()
        // 下载文件
        binding.btnDownload.setOnClickListener {
            DownloadUtil.getInstance().startDownload(
                binding.etUrl.text.toString(),
                FILE_PATH + File.separator + "test.apk",
                this
            )
        }
    }

    override fun onDownloadStart() {
        mSb.clear()
        mSb.appendLine("======> 开始下载")
        binding.tvResult.text = mSb.toString()
        showProgress()
    }

    override fun onDownloading(percent: Int) {
        mSb.appendLine("======> 下载中，已下载${percent}%")
        binding.tvResult.text = mSb.toString()
    }

    override fun onDownloadFail(e: Exception?) {
        mSb.appendLine("======> ${e?.message}")
        binding.tvResult.text = mSb.toString()
        dismissProgress()
    }

    override fun onDownloadComplete(file: File) {
        mSb.appendLine("======> 下载完成！位置：${file.path}")
        binding.tvResult.text = mSb.toString()
        dismissProgress()
    }

    override fun onDestroy() {
        super.onDestroy()
        DownloadUtil.getInstance().unRegister()
    }
}