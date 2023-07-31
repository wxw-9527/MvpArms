package com.rouxinpai.demo.di

import com.rouxinpai.arms.di.IUrlModule
import com.rouxinpai.arms.di.qualifier.GetDomainConfigurationUrl
import com.rouxinpai.arms.di.qualifier.GetUpgradeUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 10:42
 * desc   :
 */
@Module
@InstallIn(SingletonComponent::class)
object UrlModule : IUrlModule {

    @Provides
    @Singleton
    @GetDomainConfigurationUrl
    override fun provideDomainConfigurationUrl(): String {
        return "http://test.zk-work.com/stage-api/system/customer/validCode/"
    }

    @Provides
    @Singleton
    @GetUpgradeUrl
    override fun provideUpgradeUrl(): String {
        return "http://test.zk-work.com/stage-api/system/client/info"
    }
}