package com.rouxinpai.arms.model.bean

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/4/14 11:32
 * desc   :
 */
data class Sextuple<out A, out B, out C, out D, out E, out F>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E, val sixth: F
)