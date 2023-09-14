package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/24 17:47
 * desc   :
 */
class MaterialInfoVO(
    val inboundOrderDetailId: String,
    val id: String,
    val code: String,
    val name: String,
    val spec: String,
    val unit: String,
    val batchCode: String?,
    val bomCode: String?,
    val quantity: Float,
    val stockQuantity: Float?,
    val basicPackagingQuantity: Float,
    val warehouseId: String,
    val warehouseCode: String,
    val warehouseName: String?,
    val supplier: SupplierVO?
) {

    /**
     * 获取有效数量，如果库存量不为空，则返回库存量；否则返回条码数量。
     */
    val validQuantity: Float
        get() = stockQuantity ?: quantity
}