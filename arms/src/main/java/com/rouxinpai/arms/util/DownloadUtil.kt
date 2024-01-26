package com.rouxinpai.arms.util

import android.content.Context
import com.blankj.utilcode.util.PathUtils
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.OkDownload
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.file.CustomProcessFileStrategy
import com.liulishuo.okdownload.kotlin.enqueue1
import java.io.File

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/1/3 17:03
 * desc   :
 */

class DownloadUtil {

    companion object {

        @Volatile
        private var instance: DownloadUtil? = null

        /**
         * 当前类单例对象
         */
        fun getInstance(): DownloadUtil =
            instance ?: synchronized(this) {
                instance ?: DownloadUtil().also { instance = it }
            }

        // 文件存储路径
        private val PATH = PathUtils.getExternalAppFilesPath() + File.separator + "download"
    }

    // 下载任务
    private var mDownloadTask: DownloadTask? = null

    /**
     * 初始化
     */
    fun init(context: Context) {
        val okDownload = OkDownload
            .Builder(context)
            .processFileStrategy(CustomProcessFileStrategy()) // 修复Android10下载失败的问题
            .build()
        OkDownload.setSingletonInstance(okDownload)
    }

    /**
     * 开始下载
     */
    fun startDownload(
        url: String,
        fileName: String,
        listener: OnDownloadListener,
    ) {
        mDownloadTask = DownloadTask
            .Builder(url, PATH, fileName)
            .setConnectionCount(1) // 设置单线程下载，修复The current offset on block-info isn'tupdate correct,1306995!= 2380221 on 2
            .build()
        mDownloadTask?.enqueue1(
            taskStart = { _, _ ->
                listener.onDownloadStart()
            },
            progress = { _, currentOffset, totalLength ->
                val percent = currentOffset.toFloat() / totalLength.toFloat()
                listener.onDownloading(percent)
            },
            taskEnd = { task, cause, realCause, _ ->
                val file = task.file
                if (cause == EndCause.COMPLETED && file != null && file.exists()) {
                    listener.onDownloadComplete(file)
                } else {
                    listener.onDownloadFail(realCause)
                }
            }
        )
    }

    fun onDestroy() {
        mDownloadTask?.cancel()
        mDownloadTask = null
    }

    interface OnDownloadListener {
        fun onDownloadStart()
        fun onDownloading(percent: Float)
        fun onDownloadFail(e: Exception?)
        fun onDownloadComplete(file: File)
    }
}