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
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.dict.util.DictUtil
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
                add("quantity") // 库存数量
                add("quality") // 质检信息
                add("inboundNo") // 入库单信息
                add("supplierCode") // 供应商信息
                add("color") // 物料颜色
                add("sn") // sn
            })
        }.toRequestBody()
        val disposable = retrofit.create<BarcodeApi>()
            .listBarcodeInfos(body = body)
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .map { data -> data.map { PrintResultVO.fail(BarcodeInfoVO.convertFromDTO(it)) } }
            .map { data -> barcodeList.mapNotNull { barcode -> data.find { barcode == it.barcodeInfo.barcode } } }
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
                val unit = (DictUtil.getInstance().convertMaterialUnit(material.unit)?.value ?: material.unit)
                val printDataObject = JsonObject().apply {
                    addProperty("materialName", material.name)
                    addProperty("materialCode", material.code)
                    addProperty("materialColor", material.color)
                    addProperty("materialUnit", unit)
                    addProperty("barCode", barcodeInfo.barcode)
                    addProperty("receivedQuantityUnit", material.quantity.format() + unit)
                    addProperty("batchCode", material.batchCode)
                    addProperty("sn", barcodeInfo.barcode)
                    addProperty("spec", material.spec)
                    addProperty("supplier", barcodeInfo.material.supplier?.supplierName)
                    addProperty("printTime", TimeUtils.getNowString())
                }
                add("printDataObject", printDataObject)
            }.toRequestBody()
        }
        // 发起网络请求并生成图片
        val base64List = arrayListOf<String>()
        val observableList = barcodeInfoList.map { barcodeInfo ->
            val body = createRequestBody(barcodeInfo)
            retrofit.create<PrintApi>()
                .genImage(body = body)
                .compose(schedulersTransformer())
                .compose(responseTransformer())
        }
        val disposable = Observable.concat(observableList)
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