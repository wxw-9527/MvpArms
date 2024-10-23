package com.rouxinpai.demo.di

import com.rouxinpai.arms.di.IUrlModule
import com.rouxinpai.arms.di.qualifier.GetDomainUrl
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
    @GetDomainUrl
    override fun provideDomainUrl(): String {
        // return "http://dev.zk-work.com/stage-api/"
        return "http://test.zk-work.com/stage-api/"
        // return "https://admin.yzkcloud.com/stage-api/"
        // return "http://admin.manufacture.beststory.com.cn/stage-api/"
        // return "http://172.16.2.201/stage-api/"
    }
}