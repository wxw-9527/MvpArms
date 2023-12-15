package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/3 15:57
 * desc   :
 */
class SupplierVO(
    val supplierId: String,
    val supplierCode: String,
    val supplierName: String,
    val shortName: String?,
    val contact: String?,
    val contactTel: String?,
    val contactAddress: String?,
    val supplierStatus: Int,
    val remark: String?,
) {

    companion object {

        /**
         *
         */
        fun fromDTO(dto: SupplierDTO): SupplierVO {
            return SupplierVO(
                supplierId = dto.supplierId,
                supplierCode = dto.supplierCode,
                supplierName = dto.supplierName,
                shortName = dto.shortName,
                contact = dto.contact,
                contactTel = dto.contactTel,
                contactAddress = dto.contactAddress,
                supplierStatus = dto.supplierStatus,
                remark = dto.remark,
            )
        }
    }
}