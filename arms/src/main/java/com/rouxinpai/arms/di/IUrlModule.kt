package com.rouxinpai.arms.di

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 10:40
 * desc   :
 */

interface IUrlModule {

    /**
     * 主租户域名
     */
    fun provideDomainUrl(): String
}