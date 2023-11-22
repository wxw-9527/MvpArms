package com.rouxinpai.arms.print

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.printer.sdk.utils.Utils
import com.rouxinpai.arms.barcode.api.BarcodeApi
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.base.application.BaseApplication
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.extension.format
import com.rouxinpai.arms.extension.toRequestBody
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.arms.print.api.PrintApi
import com.rouxinpai.arms.print.model.DirectionEnum
import com.rouxinpai.arms.print.model.PrintResultVO
import com.rouxinpai.arms.print.model.TemplateVO
import com.rouxinpai.arms.print.util.PrintUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/11 16:13
 * desc   :
 */
class PrintPresenter @Inject constructor(@ApplicationContext val context: Context) :
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
                add("quantity") // 库存数量
                add("quality") // 质检信息
                add("inboundNo") // 入库单信息
                add("supplierCode") // 供应商信息
                add("color") // 物料颜色
            })
        }.toRequestBody()
        val disposable = retrofit.create<BarcodeApi>()
            .listBarcodeInfos(body = body)
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .map { data -> data.map { PrintResultVO.fail(BarcodeInfoVO.convertFromDTO(it)) } }
            .subscribeWith(object : DefaultObserver<List<PrintResultVO>>(view) {

                override fun onData(t: List<PrintResultVO>) {
                    super.onData(t)
                    view?.showBarcodeInfos(t)
                    view?.showSuccessPage()
                }
            })
        addDisposable(disposable)
    }

    override fun genImage(
        template: TemplateVO,
        barcodeInfo: BarcodeInfoVO,
        copies: Int,
        direction: DirectionEnum,
        index: Int,
    ) {
        val material = barcodeInfo.material
        val body = JsonObject().apply {
            addProperty("printTemplateId", template.id)
            val printDataObject = JsonObject().apply {
                addProperty("materialName", material.name)
                addProperty("materialCode", material.code)
                addProperty("materialColor", material.color)
                addProperty("barCode", barcodeInfo.barcode)
                addProperty("receivedQuantityUnit", material.quantity.format() + (context.applicationContext as BaseApplication).convertMaterialUnit(material.unit))
                addProperty("batchCode", material.batchCode)
                addProperty("sn", barcodeInfo.barcode)
                addProperty("spec", material.spec)
                addProperty("supplier", barcodeInfo.material.supplier?.supplierName)
                addProperty("copies", 1)
            }
            add("printDataObject", printDataObject)
        }.toRequestBody()
        val disposable = retrofit.create<PrintApi>()
            .genImage(body = body)
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .map { data ->
                val byteArray = Base64.decode(data, Base64.DEFAULT)
                val originalBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                val zoomedBitmap = Utils.zoomImage(originalBitmap, template.printWith.toDouble(), 0)
                val rotatedBitmap = rotateBitmap(zoomedBitmap, direction) // 旋转
                val binationalBitmap = PrintUtil.getBinationalBitmap(rotatedBitmap) // 二值化
                binationalBitmap
            }
            .subscribeWith(object : DefaultObserver<Bitmap>(view, false) {

                override fun onData(t: Bitmap) {
                    super.onData(t)
                    // 下发打印指令
                    view?.sendPrintCommand(template, t, copies, direction, index)
                }
            })
        addDisposable(disposable)
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