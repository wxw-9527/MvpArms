package com.rouxinpai.arms.extension

import java.net.URLEncoder

/**
 * 对字符串进行 URL 编码。
 *
 * @param enc 编码格式，默认为 UTF-8。
 * @return URL 编码后的字符串。
 */
fun String.urlEncode(enc: String = "UTF-8"): String {
    return URLEncoder.encode(this, enc)
}