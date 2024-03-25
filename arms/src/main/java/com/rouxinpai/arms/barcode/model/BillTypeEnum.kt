package com.rouxinpai.arms.barcode.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/14 11:15
 * desc   :
 */
enum class BillTypeEnum(val billTypeCode: String) {

    /**
     * 生产订单号
     */
    ORDER_CODE("taskNo"),

    /**
     * 排产单号
     */
    ARRANGE_CODE("arrangeNo"),

    /**
     * 工位任务单号
     */
    STATION_TASK_CODE("stationTaskNo"),

    /**
     * 交货单号
     */
    INBOUND_CODE("inboundCode"),

    /**
     * 入库单号
     */
    STORAGE_CODE("storageCode"),

    /**
     * 采购单号
     */
    PURCHASE_CODE("purchaseCode"),

    /**
     * 拣货单号
     */
    PICK_CODE("pickCode"),

    /**
     * 出库单号
     */
    OUTBOUND_CODE("outboundCode"),

    /**
     * 父级条码
     */
    PARENT_BAR_CODE("parentBarCode"),

    /**
     * 物料编码
     */
    MATERIAL_CODE("materialCode"),

    /**
     * 供应商编码
     */
    SUPPLIER_CODE("supplierCode"),

    /**
     * 批次码
     */
    BATCH_CODE("batchCode"),

    /**
     * BOM编码
     */
    BOM_CODE("bomCode"),

    /**
     * 仓库编码
     */
    WAREHOUSE_CODE("warehouseCode"),

    /**
     * 质检单号
     */
    QUALITY_CODE("qualityCode"),

    /**
     * 序列号
     */
    SN("sn"),

    /**
     * 合格数量
     */
    CONFORMITY_COUNT("conformityCount"),

    /**
     * 不合格数量
     */
    UNCONFORMITY_COUNT("unconformityCount"),

    /**
     * sn开始
     */
    // SN_START("snStart"),

    /**
     * sn结束
     */
    // SN_END("snEnd"),

    /**
     * 排产单下发
     */
    // ARRANGE_ISSUE("arrangeIssue"),

    /**
     * 库存数量
     */
    QUANTITY("quantity"),

    /**
     * 拣货数量
     */
    PICK_QUANTITY("pickQuantity");
}