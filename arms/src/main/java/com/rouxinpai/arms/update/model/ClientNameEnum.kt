package com.rouxinpai.arms.update.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 9:53
 * desc   :
 */
enum class ClientNameEnum(val value: String) {

    /**
     * 仓储
     */
    WMS("wms"),

    /**
     * 生产看板
     */
    WORKPAD("workpad"),

    /**
     * 质检
     */
    QC("qc"),

    /**
     * 设备管理
     */
    DM("dm");
}