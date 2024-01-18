package com.rouxinpai.arms.util

import android.content.Context
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.DownloadTaskListener
import com.arialyy.aria.core.task.DownloadTask
import java.io.File
import java.io.FileNotFoundException

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/1/3 17:03
 * desc   :
 */

class DownloadUtil: DownloadTaskListener {

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

        // 默认任务ID
        private const val DEFAULT_TASK_ID: Long = -1
    }

    // 下载监听
    private var mOnDownloadListener: OnDownloadListener? = null

    // 任务ID
    private var mTaskId: Long = DEFAULT_TASK_ID

    /**
     * 初始化
     */
    fun init(context: Context) {
        // 初始化Aria
        Aria.init(context)
    }

    /**
     * 注册
     */
    fun register() {
        Aria.download(this).register()
    }

    /**
     * 反注册
     */
    fun unRegister() {
        Aria.download(this).unRegister()
    }

    /**
     * 开始下载
     */
    fun startDownload(url: String, filePath: String, listener: OnDownloadListener) {
        // 设置下载监听
        mOnDownloadListener = listener
        // 开始下载
        mTaskId = Aria.download(this)
            .load(url)
            .setFilePath(filePath)
            .ignoreCheckPermissions()
            .create()
    }

    override fun onWait(task: DownloadTask?) {}

    /**
     * 预处理的注解，在任务未开始前回调（一般在此处预处理UI界面）
     */
    override fun onPre(task: DownloadTask?) {
        mOnDownloadListener?.onDownloadStart()
    }

    override fun onTaskPre(task: DownloadTask?) {}

    override fun onTaskResume(task: DownloadTask?) {}

    /**
     * 任务开始时的注解，新任务开始时进行回调
     */
    override fun onTaskStart(task: DownloadTask?) {}

    override fun onTaskStop(task: DownloadTask?) {}

    override fun onTaskCancel(task: DownloadTask?) {}

    /**
     * 任务失败时的注解，任务执行失败时进行回调
     */
    override fun onTaskFail(task: DownloadTask?, e: Exception?) {
        mOnDownloadListener?.onDownloadFail(e)
    }

    /**
     * 	任务完成时的注解，任务完成时进行回调
     */
    override fun onTaskComplete(task: DownloadTask) {
        val file = File(task.filePath)
        if (file.isFile && file.exists()) {
            mOnDownloadListener?.onDownloadComplete(file)
        } else {
            mOnDownloadListener?.onDownloadFail(FileNotFoundException("文件不存在！"))
        }
    }

    /**
     * 任务执行时的注解，任务正在执行时进行回调
     */
    override fun onTaskRunning(task: DownloadTask) {
        mOnDownloadListener?.onDownloading(task.percent)
    }

    override fun onNoSupportBreakPoint(task: DownloadTask?) {}

    interface OnDownloadListener {
        fun onDownloadStart()
        fun onDownloading(percent: Int)
        fun onDownloadFail(e: Exception?)
        fun onDownloadComplete(file: File)
    }
}