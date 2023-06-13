package com.rouxinpai.arms.domain.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 14:12
 * desc   :
 */
data class DomainConfigurationDTO(
    val captchaCode: String,
    val customerName: String,
    val customerShortName: String?,
    val customerEnglishName: String?,
    val customerEnglishShortName: String?,
    val website: String?,
    val logoUrl: String?,
    val domain: String
)