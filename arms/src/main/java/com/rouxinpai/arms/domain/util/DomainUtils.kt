package com.rouxinpai.arms.domain.util

import com.rouxinpai.arms.domain.model.DomainConfigurationVO
import com.tencent.mmkv.MMKV

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 10:29
 * desc   :
 */
object DomainUtils {

    private val mDefaultMMKV: MMKV by lazy(LazyThreadSafetyMode.NONE) {
        MMKV.defaultMMKV()
    }

    /**
     * 清除全部数据
     */
    fun clearAll() {
        // 清除 DomainConfigurationVO 对象
        mDomainConfiguration = null
        // 清除 AccessToken
        mAccessToken = null
        // 清除 MMKV
        mDefaultMMKV.clearAll()
    }

    // 常量，用于存储和获取 DomainConfigurationVO 对象的键
    private const val KEY_DOMAIN_CONFIGURATION = "key_domain_configuration"

    // 保存 DomainConfigurationVO 对象的实例变量
    private var mDomainConfiguration: DomainConfigurationVO? = null

    /**
     * 保存域名配置信息
     */
    fun setDomainConfiguration(domainConfiguration: DomainConfigurationVO) {
        mDefaultMMKV.encode(KEY_DOMAIN_CONFIGURATION, domainConfiguration)
    }

    /**
     * 获取域名配置信息
     */
    fun getDomainConfiguration(): DomainConfigurationVO? {
        if (mDomainConfiguration == null) {
            mDomainConfiguration = mDefaultMMKV.decodeParcelable(
                KEY_DOMAIN_CONFIGURATION, DomainConfigurationVO::class.java
            )
        }
        return mDomainConfiguration
    }

    /**
     *
     */
    fun getDomain(): String? {
        return getDomainConfiguration()?.domain
    }

    // AccessToken
    private const val KEY_ACCESS_TOKEN = "key_access_token"

    private var mAccessToken: String? = null

    fun setAccessToken(accessToken: String) {
        mDefaultMMKV.encode(KEY_ACCESS_TOKEN, accessToken)
    }

    fun getAccessToken(): String? {
        if (mAccessToken == null) {
            mAccessToken = mDefaultMMKV.decodeString(KEY_ACCESS_TOKEN)
        }
        return mAccessToken
    }
}