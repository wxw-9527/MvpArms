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
    val contact: String?,
    val contactTel: String?,
    val contactAddress: String?,
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
                contact = dto.contact,
                contactTel = dto.contactTel,
                contactAddress = dto.contactAddress,
            )
        }
    }
}