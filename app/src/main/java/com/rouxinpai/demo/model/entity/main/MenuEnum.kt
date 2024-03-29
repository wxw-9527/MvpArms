package com.rouxinpai.demo.model.entity.main

import com.rouxinpai.arms.nfc.QrCodeToNfcActivity
import com.rouxinpai.demo.feature.demo.balloon.BalloonActivity
import com.rouxinpai.demo.feature.demo.barcode.BarcodeActivity
import com.rouxinpai.demo.feature.demo.dialog.DialogActivity
import com.rouxinpai.demo.feature.demo.dict.DictActivity
import com.rouxinpai.demo.feature.demo.dropdown.DropdownActivity
import com.rouxinpai.demo.feature.demo.filedownload.FileDownloadActivity
import com.rouxinpai.demo.feature.demo.fragment.FragmentDemoActivity
import com.rouxinpai.demo.feature.demo.notice.MessageDemoActivity
import com.rouxinpai.demo.feature.demo.openimage.OpenImageActivity
import com.rouxinpai.demo.feature.demo.pictureselector.PictureSelectorDemoActivity
import com.rouxinpai.demo.feature.demo.print.PrintDemoActivity
import com.rouxinpai.demo.feature.demo.repeatclick.DebounceClickDemoActivity
import com.rouxinpai.demo.feature.demo.rv.RvLoadMoreActivity
import com.rouxinpai.demo.feature.demo.search.SearchEdittextDemoActivity
import com.rouxinpai.demo.feature.demo.switchdomain.SwitchDomainActivity
import com.rouxinpai.demo.feature.demo.upgrade.UpgradeActivity

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/7/17 10:13
 * desc   :
 */
enum class MenuEnum(val value: String, val clazz: Class<*>) {

    /**
     * 切换域名
     */
    SWITCH_DOMAIN(value = "切换域名", SwitchDomainActivity::class.java),

    /**
     * 对话框
     */
    DIALOG(value = "对话框", clazz = DialogActivity::class.java),

    /**
     * 气泡提示
     */
    BALLOON(value = "气泡提示", clazz = BalloonActivity::class.java),

    /**
     * 下拉框
     */
    DROPDOWN(value = "下拉框", clazz = DropdownActivity::class.java),

    /**
     * 版本更新
     */
    UPGRADE(value = "版本更新", clazz = UpgradeActivity::class.java),

    /**
     *
     */
    FRAGMENT_DEMO(value = "Fragment示例", clazz = FragmentDemoActivity::class.java),

    /**
     * 防抖点击
     */
    DEBOUNCE_CLICK_DEMO(value = "防抖点击", clazz = DebounceClickDemoActivity::class.java),

    /**
     * 打印示例
     */
    PRINT_DEMO(value = "打印示例", clazz = PrintDemoActivity::class.java),

    /**
     * 搜索输入框
     */
    SEARCH_EDITTEXT(value = "搜索输入框", clazz = SearchEdittextDemoActivity::class.java),

    /**
     * 消息推送
     */
    MESSAGE_DEMO(value = "消息推送", clazz = MessageDemoActivity::class.java),

    /**
     * 读NFC标签
     */
    READ_NFC(value = "绑定NFC标签", clazz = QrCodeToNfcActivity::class.java),

    /**
     * 条码解析
     */
    BARCODE(value = "条码解析", clazz = BarcodeActivity::class.java),

    /**
     * 列表-加载更多
     */
    RV_LOAD_MORE(value = "列表-加载更多", clazz = RvLoadMoreActivity::class.java),

    /**
     * 字典
     */
    DICT(value = "字典", clazz = DictActivity::class.java),

    /**
     * 图片预览
     */
    OPEN_IMAGE(value = "图片预览", clazz = OpenImageActivity::class.java),

    /**
     * 文件下载
     */
    FILE_DOWNLOAD(value = "文件下载", clazz = FileDownloadActivity::class.java),

    /**
     * 图片选择
     */
    PICTURE_SELECTOR(value = "图片选择", clazz = PictureSelectorDemoActivity::class.java)
}