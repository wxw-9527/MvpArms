package com.rouxinpai.arms.update.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/27 16:43
 * desc   :
 */
data class UpdateInfo(
    val id: String,
    val clientName: String,
    val clientVersion: String,
    val clientVersionCode: Int,
    val clientLog: String,
    val forceUpload: Int,
    val clientType: String,
    val channelFlag: String,
    val sysClientFiles: List<SysClientFile>,
) {

    companion object {

        // 强制更新标志
        private const val FORCE_UPDATE = 0
    }

    /**
     * 是否强制更新
     */
    val isForceUpgrade: Boolean
        get() = FORCE_UPDATE == forceUpload

    /**
     * 文件名
     * 实例：wms-1.0.0.apk
     */
    val fileName: String
        get() = "${clientName}-${clientVersion}.apk"

    /**
     * 文件下载链接
     */
    val apkFileUrl: String
        get() = sysClientFiles.first().fileUrl
}