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
    val materialStockDetailVoList: List<MaterialStockDetailVO>?,
    val outboundOrderDetailRecordVo: OutboundOrderDetailRecordVO?
) {

    /**
     * 物料总库存
     */
    val totalStorageQuantity: Float?
        get() = materialStockDetailVoList?.map { it.storageQuantity }?.sum()
}