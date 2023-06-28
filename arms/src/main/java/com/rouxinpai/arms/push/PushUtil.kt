package com.rouxinpai.arms.push

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.igexin.sdk.PushManager
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/28 16:00
 * desc   :
 */
object PushUtil {

    /**
     * 初始化推送
     */
    fun init(context: Context, debug: Boolean) {
        if (debug) {
            PushManager.getInstance().setDebugLogger(context) { log ->
                Timber.tag("PUSH_LOG").d(log)
            }
        }
        PushManager.getInstance().initialize(context)
    }

    /**
     * 打开 Push 推送，默认是开启状态
     */
    fun turnOnPush(context: Context) {
        PushManager.getInstance().turnOnPush(context)
    }

    /**
     * 关闭 Push 推送
     */
    fun turnOffPush(context: Context) {
        PushManager.getInstance().turnOffPush(context)
    }

    /**
     * 获取当前 SDK 的服务状态
     * @return 服务状态 true：当前推送已打开；false：当前推送已关闭
     */
    fun isPushTurnedOn(context: Context): Boolean {
        return PushManager.getInstance().isPushTurnedOn(context)
    }

    /**
     * 获取当前用户缓存的ClientId（长时间未登录或 AppId 发生变化造成的 cid 变化以GTIntentService中onReceiveClientId方法返回的ClientId为准）
     * @return 当前用户的 cid，如果未初始化成功，返回 null
     */
    fun getClientId(context: Context): String {
        return PushManager.getInstance().getClientid(context)
    }

    /**
     * 绑定别名
     * @param alias 别名名称
     */
    fun bindAlias(context: Context, alias: String) {
        PushManager.getInstance().bindAlias(context, alias)
    }

    /**
     * 解绑别名
     * @param alias 别名名称
     * @param isSelf 是否只对当前 cid 有效，如果是 true，只对当前 cid 做解绑；如果是 false，对所有绑定该别名的 cid 列表做解绑
     */
    fun unBindAlias(context: Context, alias: String, isSelf: Boolean = false) {
        PushManager.getInstance().unBindAlias(context, alias, isSelf)
    }

    /**
     *
     * 生成打开特定页面地址
     * @param context
     * @param cls 要跳转的页面
     * @param packageName 目标应用的包名
     * @param urlString Scheme协议（demoscheme://com.rouxinpai.demo/main?）开发者可以自定义
     * @param map intent 中添加自定义键值对
     */
    fun genIntentUri(
        context: Context,
        cls: Class<*>,
        packageName: String,
        urlString: String,
        map: Map<String, Any>
    ): String {
        val intent = Intent(context, cls)
        // 如果设置了 package，则表示目标应用必须是 package 所对应的应用
        intent.setPackage(packageName)
        // Scheme协议（gtpushscheme://com.getui.push/detail?）开发者可以自定义
        intent.data = Uri.parse(urlString)
        // gttask 不用赋值，添加 gttask 字段后，个推给客户端的 intent 里会自动拼接上 taskid 和 actionid
        intent.putExtra("gttask", "")
        // intent 中添加自定义键值对
        map.forEach {
            val key = it.key
            val value = it.value
            if (value is String) {
                intent.putExtra(key, value)
            }
            if (value is Int) {
                intent.putExtra(key, value)
            }
            if (value is Float) {
                intent.putExtra(key, value)
            }
            if (value is Double) {
                intent.putExtra(key, value)
            }
            if (value is Boolean) {
                intent.putExtra(key, value)
            }
            if (value is Byte) {
                intent.putExtra(key, value)
            }
        }
        // 应用必须带上该Flag，如果不添加该选项有可能会显示重复的消息，强烈推荐使用Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return intent.toUri(Intent.URI_INTENT_SCHEME)
    }
}