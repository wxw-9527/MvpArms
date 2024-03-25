package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/25 15:12
 * desc   :
 */
data class InboundOrderDetailRecordVO(
    val id: String,
    val inboundOrderRecordId: String,
    val inboundOrderDetailId: String,
    val barCode: String,
    val actualQuantity: Float,
) {

    companion object {

        /**
         *
         */
        fun fromDto(dto: InboundOrderDetailRecordDTO): InboundOrderDetailRecordVO {
            return InboundOrderDetailRecordVO(
                id = dto.id,
                inboundOrderRecordId = dto.inboundOrderRecordId,
                inboundOrderDetailId = dto.inboundOrderDetailId,
                barCode = dto.barCode,
                actualQuantity = dto.actualQuantity,
            )
        }
    }
}