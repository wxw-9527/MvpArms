package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/8/29 下午2:20
 * desc   :
 */
data class InboundOrderDetailVO(
    val id: String,
    val inboundOrderId: String,
    val materialId: String,
    val materialCode: String,
    val batchCode: String,
    val furnaceNo: String?,
) {

    companion object {

        /**
         * 将[InboundOrderDetailDTO]转换成[InboundOrderDetailVO]
         */
        fun fromDto(dto: InboundOrderDetailDTO): InboundOrderDetailVO {
            return InboundOrderDetailVO(
                id = dto.id,
                inboundOrderId = dto.inboundOrderId,
                materialId = dto.materialId,
                materialCode = dto.materialCode,
                batchCode = dto.batchCode,
                furnaceNo = dto.furnaceNo,
            )
        }
    }
}