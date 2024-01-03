package com.rouxinpai.demo.feature.demo.filedownload

import android.os.Bundle
import com.blankj.utilcode.util.PathUtils
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.demo.databinding.FileDownloadActivityBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/12/28 15:59
 * desc   :
 */
class FileDownloadActivity : BaseActivity<FileDownloadActivityBinding>() {

    companion object {

        // 分隔符
        private const val DELIMITER = "/"

        // 文件存储位置
        private val FILE_PATH: String = PathUtils.getExternalAppFilesPath()
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 下载文件
        binding.btnDownload.setOnClickListener {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}