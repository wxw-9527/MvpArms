package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/24 17:47
 * desc   :
 */
class MaterialInfoVO(
    val id: String,
    val code: String,
    val name: String,
    val spec: String,
    val unit: String?,
    val supplier: SupplierVO?,
    val inboundOrderDetailRecordVo: InboundOrderDetailRecordVO?,
    val materialStockDetailVoList: List<MaterialStockDetailVO>?,
    val outboundOrderDetailRecordVo: OutboundOrderDetailRecordVO?,
) {

    /**
     * 收货数
     */
    val receiveQuantity: Float?
        get() = inboundOrderDetailRecordVo?.actualQuantity

    /**
     * 物料库存总数
     */
    val totalStorageQuantity: Float?
        get() = materialStockDetailVoList?.map { it.storageQuantity }?.sum()

    /**
     * 拣货数量
     */
    val pickQuantity: Float?
        get() = outboundOrderDetailRecordVo?.actualQuantity
}