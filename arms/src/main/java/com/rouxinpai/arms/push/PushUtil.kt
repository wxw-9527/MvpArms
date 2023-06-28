package com.rouxinpai.arms.push

import android.content.Context
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
}