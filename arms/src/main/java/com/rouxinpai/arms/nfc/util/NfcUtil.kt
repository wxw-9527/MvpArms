@file:Suppress("DEPRECATION")

package com.rouxinpai.arms.nfc.util

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import okhttp3.internal.and
import timber.log.Timber
import java.io.IOException
import java.nio.charset.Charset
import java.util.Arrays
import java.util.Locale
import kotlin.text.Charsets.UTF_16
import kotlin.text.Charsets.UTF_8

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/7 14:04
 * desc   :
 */
object NfcUtil {

    /**
     * 标志位长度
     */
    const val FLAG_LENGTH = 2

    /**
     * 读取NFC标签文本数据
     */
    fun readNfcTag(intent: Intent?): String? {
        if (intent == null) return null
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val rawMsgArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val msgArray = rawMsgArray?.map { rawMsg -> rawMsg as NdefMessage }
            if (msgArray != null) {
                val record = msgArray.first().records.first()
                return parseTextRecord(record)
            }
        }
        return null
    }

    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     * @param ndefRecord
     * @return
     */
    private fun parseTextRecord(ndefRecord: NdefRecord): String? {
        // 判断TNF
        if (ndefRecord.tnf != NdefRecord.TNF_WELL_KNOWN) return null
        // 判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.type, NdefRecord.RTD_TEXT)) return null
        // 获得字节数组，然后进行分析
        val payload = ndefRecord.payload
        // 下面开始NDEF文本数据第一个字节，状态字节
        // 判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
        // 其他位都是0，所以进行"位与"运算后就会保留最高位
        val textEncoding = if (payload[0] and 0x80 == 0) UTF_8 else UTF_16
        // 3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
        val languageCodeLength = payload[0] and 0x3f
        // 下面开始NDEF文本数据第二个字节，语言编码
        // 获得语言编码
        return String(payload, languageCodeLength + 1, payload.size - languageCodeLength - 1, textEncoding)
    }

    /**
     * 写数据
     * @param intent 意图
     * @param text 需要写入的文本
     * @return 写入结果 true：成功 false：失败
     */
    fun writeTag(intent: Intent?, text: String?): Boolean {
        if (intent == null || text.isNullOrBlank()) return false
        val detectedTag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return false
        val ndefRecord = createTextRecord(text)
        val ndefMessage = NdefMessage(arrayOf(ndefRecord))
        val ndef = Ndef.get(detectedTag)
        if (ndef != null) {
            try {
                ndef.connect()
                ndef.writeNdefMessage(ndefMessage)
                return true
            } catch (e: IOException) {
                Timber.tag(this::class.java.simpleName).e(e)
            }
        }
        return false
    }

    /**
     * 创建NDEF文本数据
     * @param text
     * @return
     */
    private fun createTextRecord(text: String): NdefRecord {
        val langBytes = Locale.CHINESE.language.toByteArray(Charset.forName("US-ASCII"))
        val utfEncoding = UTF_8
        // 将文本转换为UTF-8格式
        val textBytes = text.toByteArray(utfEncoding)
        // 设置状态字节编码最高位数为0
        val utfBit = 0
        // 定义状态字节
        val status = (utfBit + langBytes.size).toChar()
        val data = ByteArray(1 + langBytes.size + textBytes.size)
        // 设置第一个状态字节，先将状态码转换成字节
        data[0] = status.toByte()
        // 设置语言编码，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1到langBytes.length的位置
        System.arraycopy(langBytes, 0, data, 1, langBytes.size)
        // 设置文本字节，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1+langBytes.length到textBytes.length的位置
        System.arraycopy(textBytes, 0, data, 1 + langBytes.size, textBytes.size)
        //通过字节传入NdefRecord对象
        //NdefRecord.RTD_TEXT：传入类型 读写
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), data)
    }
}