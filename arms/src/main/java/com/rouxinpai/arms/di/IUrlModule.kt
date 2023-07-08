package com.rouxinpai.arms.di

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 10:40
 * desc   :
 */

interface IUrlModule {

    /**
     * 获取域名配置地址
     */
    fun provideDomainConfigurationUrl(): String

    /**
     *
     */
    fun provideUpgradeUrl(): String
}