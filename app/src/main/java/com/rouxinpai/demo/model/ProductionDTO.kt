package com.rouxinpai.demo.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/1 18:21
 * desc   :
 */
data class ProductionDTO(
    val taskNum: String,
    val batchNum: String,
    val finishCount: Int,
    val produceCount: Int,
    val startSn: String,
    val endSn: String,
    val completeMaterialStart: Int,
    val workStatus: Int,
)