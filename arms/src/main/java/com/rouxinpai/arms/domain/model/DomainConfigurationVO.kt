package com.rouxinpai.arms.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 14:13
 * desc   :
 */
@Parcelize
class DomainConfigurationVO(
    val customerName: String,
    val customerShortName: String?,
    val customerEnglishName: String?,
    val customerEnglishShortName: String?,
    val logoUrl: String?,
    val domain: String,
) : Parcelable {

    companion object {

        /**
         * DTO 转 VO
         */
        fun fromDTO(dto: DomainConfigurationDTO): DomainConfigurationVO {
            return DomainConfigurationVO(
                customerName = dto.customerName,
                customerShortName = dto.customerShortName,
                customerEnglishName = dto.customerEnglishName,
                customerEnglishShortName = dto.customerEnglishShortName,
                logoUrl = dto.logoUrl,
                domain = dto.domain,
            )
        }
    }
}