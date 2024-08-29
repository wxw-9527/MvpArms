package com.rouxinpai.arms.print

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.printer.sdk.utils.Utils
import com.rouxinpai.arms.barcode.api.BarcodeApi
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.barcode.model.BillTypeEnum
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.extension.format
import com.rouxinpai.arms.extension.toRequestBody
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.arms.print.api.PrintApi
import com.rouxinpai.arms.print.model.ContentTypeEnum.*
import com.rouxinpai.arms.print.model.DirectionEnum
import com.rouxinpai.arms.print.model.PrintResultVO
import com.rouxinpai.arms.print.model.TemplateVO
import com.rouxinpai.arms.print.util.PrintUtil
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/11 16:13
 * desc   :
 */
class PrintPresenter @Inject constructor() :
    BasePresenter<PrintContract.View>(),
    PrintContract.Presenter {

    override fun getTemplate(): TemplateVO? {
        return PrintUtil.getTemplate()
    }

    override fun listBarcodeInfos(barcodeList: List<String>) {
        view?.showLoadingPage()
        val body = JsonObject().apply {
            val barCodes = JsonArray().apply { barcodeList.forEach { barcode -> add(barcode) } }
            add("barCodes", barCodes)
            add("billTypes", JsonArray().apply {
                add(BillTypeEnum.PURCHASE_CODE.billTypeCode)
                add(BillTypeEnum.SUPPLIER_CODE.billTypeCode)
                add(BillTypeEnum.BATCH_CODE.billTypeCode)
                add(BillTypeEnum.BOM_CODE.billTypeCode)
                add(BillTypeEnum.WAREHOUSE_CODE.billTypeCode)
                add(BillTypeEnum.CONFORMITY_COUNT.billTypeCode)
                add(BillTypeEnum.QUANTITY.billTypeCode)
                add(BillTypeEnum.RECEIVE_QUANTITY.billTypeCode)
                add(BillTypeEnum.PICK_QUANTITY.billTypeCode)
                add(BillTypeEnum.INBOUND_ORDER.billTypeCode)
            })
        }.toRequestBody()
        val disposable = retrofit.create<BarcodeApi>()
            .listBarcodeInfos(body = body)
            .compose(responseTransformer())
            .map { data -> data.map { PrintResultVO.fail(BarcodeInfoVO.convertFromDTO(it)) } }
            .map { data -> barcodeList.mapNotNull { barcode -> data.find { barcode == it.barcodeInfo.barcode } } }
            .compose(schedulersTransformer())
            .subscribeWith(object : DefaultObserver<List<PrintResultVO>>(view) {

                override fun onData(t: List<PrintResultVO>) {
                    super.onData(t)
                    view?.showBarcodeInfos(t)
                    view?.showSuccessPage()
                }
            })
        addDisposable(disposable)
    }

    override fun genImages(
        template: TemplateVO,
        barcodeInfoList: List<BarcodeInfoVO>,
        copies: Int,
        direction: DirectionEnum
    ) {
        // 创建请求体的函数
        fun createRequestBody(barcodeInfo: BarcodeInfoVO): RequestBody {
            val material = barcodeInfo.material
            return JsonObject().apply {
                addProperty("printTemplateId", template.id)
                val printDataObject = JsonObject().apply {
                    template.contentList.forEach { content ->
                        when (content.typeEnum) {
                            STATIC_TEXT -> addProperty(content.sourceKey, content.text) // 填充静态文本
                            TEXT -> addProperty(content.sourceKey, barcodeInfo.getBarContextData(content.sourceKey)) // batch_code、warehouse_code、purchase_code、conformity_count
                            QRCODE -> Unit // 不处理
                        }
                    }
                    addProperty("barCode", barcodeInfo.barcode)
                    addProperty("sn", barcodeInfo.displayBarcode)
                    addProperty("materialCode", material.code) // 物料编码
                    addProperty("materialName", material.name) // 物料名称
                    addProperty("materialUnit", material.unit) // 物料单位
                    addProperty("spec", material.spec) // 规格型号
                    addProperty("materialColor", barcodeInfo.bomVO?.color) // 颜色
                    addProperty("supplier", material.supplier?.supplierName) // 供应商名称
                    addProperty("printTime", TimeUtils.getNowString()) // 打印时间
                    addProperty("printDate", TimeUtils.millis2String(TimeUtils.getNowMills(), "yyyy/MM/dd")) // 打印日期
                    addProperty("currentWarehouseCode", material.materialStockDetailVoList?.joinToString("，") { it.warehouseCode }) // 物料当前存放库位编码
                    addProperty("currentWarehouseName", material.materialStockDetailVoList?.mapNotNull { it.warehouseName }?.joinToString("，")) // 物料当前存放库位名
                    addProperty("materialCraftVersion", material.materialCraftVersion) // 物料图纸版本
                    addProperty("receiver", material.receiver) // 收货方
                    addProperty("furnaceNum", material.inboundOrderDetailVO?.furnaceNo) // 炉号
                    // 收货相关
                    val receiveQuantity = material.receiveQuantity
                    if (receiveQuantity != null) {
                        addProperty("receiveQuantity", receiveQuantity.format()) // 收货数量
                        addProperty("receivedQuantityUnit", receiveQuantity.format() + material.unit.orEmpty()) // 收货数量及单位
                    }
                    // 拣货出库相关
                    val outboundOrderDetailRecordVo = material.outboundOrderDetailRecordVo
                    if (outboundOrderDetailRecordVo != null) {
                        val pickQuantity = outboundOrderDetailRecordVo.actualQuantity
                        addProperty("outboundQuantity", pickQuantity.format()) // 出库数量
                        addProperty("outboundQuantityUnit", pickQuantity.format() + material.unit.orEmpty()) // 出库数量及单位
                        addProperty("pickedFromWarehouseCode", outboundOrderDetailRecordVo.warehouseCode) // 拣出库位编码
                        addProperty("pickedFromWarehouseName", outboundOrderDetailRecordVo.warehouseName) // 拣出库位名
                    }
                    addProperty("outboundTime", material.outboundOrderDetailRecordVo?.outboundTime) // 出库时间
                }
                add("printDataObject", printDataObject) // 排产数量(template_arrange_number)、排产批次号(template_arrange_batch_num)字段暂无
            }.toRequestBody()
        }
        // 发起网络请求并生成图片
        val base64List = arrayListOf<String>()
        val observableList = barcodeInfoList.map { barcodeInfo ->
            val body = createRequestBody(barcodeInfo)
            retrofit.create<PrintApi>()
                .genImage(body = body)
                .compose(responseTransformer())
        }
        val disposable = Observable
            .concat(observableList)
            .compose(schedulersTransformer())
            .subscribeWith(object : DefaultObserver<String>(view, false) {

                override fun onData(t: String) {
                    super.onData(t)
                    base64List.add(t)
                }

                override fun onComplete() {
                    super.onComplete()
                    view?.sendPrintCommand(template, base64List, copies, direction)
                }
            })
        addDisposable(disposable)
    }

    override fun base64ToBitmap(
        base64: String,
        newWidth: Double,
        direction: DirectionEnum
    ): Bitmap {
        val byteArray = Base64.decode(base64, Base64.DEFAULT)
        val originalBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val zoomedBitmap = Utils.zoomImage(originalBitmap, newWidth, 0)
        val rotatedBitmap = rotateBitmap(zoomedBitmap, direction) // 旋转
        return PrintUtil.getBinationalBitmap(rotatedBitmap) // 二值化
    }

    /**
     * 旋转Bitmap
     */
    private fun rotateBitmap(bitmap: Bitmap, direction: DirectionEnum): Bitmap {
        return when (direction) {
            DirectionEnum.VERTICAL -> {
                val matrix = Matrix()
                matrix.postRotate(90f)
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
            else -> bitmap
        }
    }
}