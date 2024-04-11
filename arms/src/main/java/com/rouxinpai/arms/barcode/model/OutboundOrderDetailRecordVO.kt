package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/25 13:41
 * desc   :
 */
data class OutboundOrderDetailRecordVO(
    val id: String,
    val outboundRecordId: String,
    val outboundOrderDetailId: String,
    val barCode: String,
    val materialCode: String,
    val outboundTime: String,
    val actualQuantity: Float,
    val warehouseCode: String?,
    val warehouseName: String?,
) {

    companion object {

        /**
         *
         */
        fun fromDto(dto: OutboundOrderDetailRecordDTO): OutboundOrderDetailRecordVO {
            return OutboundOrderDetailRecordVO(
                id = dto.id,
                outboundRecordId = dto.outboundRecordId,
                outboundOrderDetailId = dto.outboundOrderDetailId,
                barCode = dto.barCode,
                materialCode = dto.materialCode,
                outboundTime = dto.outboundTime,
                actualQuantity = dto.actualQuantity,
                warehouseCode = dto.warehouseVO?.code,
                warehouseName = dto.warehouseVO?.name
            )
        }
    }
}