@file:Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")

package com.rouxinpai.arms.message

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.rouxinpai.arms.R
import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.arms.extension.asJsonObjectOrNull
import com.rouxinpai.arms.extension.asStringOrNull
import com.rouxinpai.arms.message.model.ClientMessageEvent
import com.rouxinpai.arms.message.model.FunctionEnum
import com.rouxinpai.arms.message.model.UnreadCountRefreshEvent
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/30 9:50
 * desc   :
 */
@AndroidEntryPoint
abstract class BaseWebSocketService : IntentService(SERVICE_NAME) {

    companion object {

        // 日志标签
        private const val SERVICE_NAME = "WebSocketService"

        // 前台通知相关
        private const val FOREGROUND_CHANNEL_ID = "foreground_service" // 通知渠道ID
        private const val FOREGROUND_CHANNEL_NAME = "通知服务" // 通知渠道名称
        private const val FOREGROUND_CONTENT_TITLE = "消息中心" // 通知标题
        private const val FOREGROUND_NOTIFICATION_ID = 1 // 通知ID

        // WebSocket相关
        private const val WS_URL = "websocket"
        private const val WS_HEADER_NAME = "Sec-WebSocket-Protocol"
        private const val WS_CLOSE = 1000 // 断开连接

        // 消息通知相关
        private const val NOTIFICATION_CHANNEL_ID = "notification" // 通知渠道ID
        private const val NOTIFICATION_CHANNEL_NAME = "消息通知" // 通知渠道名称
    }

    // OkHttpClient实例
    @Inject
    lateinit var okHttpClient: OkHttpClient

    // Gson实例
    @Inject
    lateinit var gson: Gson

    // WebSocket实例
    private var mWebSocket: WebSocket? = null

    //
    private var mDisposable: Disposable? = null

    /**
     * 首次创建服务时，系统会（在调用 onStartCommand() 或 onBind() 之前）调用此方法来执行一次性设置程序。
     * 如果服务已在运行，则不会调用此方法。
     */
    override fun onCreate() {
        Timber.tag(SERVICE_NAME).d("------> 首次创建服务")
        super.onCreate()
        // 注册EventBus
        EventBus.getDefault().register(this)
        // 创建长连接
        newWebSocket()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ClientMessageEvent) {
        mWebSocket?.send(event.message)
    }

    // 建立长链接
    private fun newWebSocket() {
        val mWebSocketRequest: Request = Request.Builder()
            .url("${DomainUtils.getDomain()}$WS_URL?token=${DomainUtils.getAccessToken().orEmpty()}")
            .build()
        mWebSocket = okHttpClient.newWebSocket(mWebSocketRequest, mWebSocketListener)
    }

    // 关闭长链接
    private fun closeWebSocket() {
        val closedSuccessfully = mWebSocket?.close(WS_CLOSE, "断开长连接")
        if (false == closedSuccessfully) {
            canceled()
        }
    }

    // OkHttp WebSocket 回调
    private val mWebSocketListener = object : WebSocketListener() {

        // 当连接建立成功后，即可在此执行发送消息等操作
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            onOpen(this@BaseWebSocketService, webSocket, response)
        }

        // 接收到服务端发来的消息时，执行对应业务逻辑
        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            onMessage(this@BaseWebSocketService, webSocket, text)
        }

        // 连接关闭
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            onClosed(this@BaseWebSocketService, webSocket, code, reason)
        }

        // 连接过程中遇到异常时，在此进行相应处理
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            onFailure(this@BaseWebSocketService, webSocket, t, response)
        }
    }

    // 销毁资源
    private fun canceled() {
        // 关闭连接
        mWebSocket?.cancel()
        mWebSocket = null
    }

    /**
     * 当另一个组件（如 Activity）请求启动服务时，系统会通过调用 startService() 来调用此方法。
     * 执行此方法时，服务即会启动并可在后台无限期运行。如果您实现此方法，则在服务工作完成后，您需负责通过调用 stopSelf() 或 stopService() 来停止服务。
     * （如果您只想提供绑定，则无需实现此方法。）
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag(SERVICE_NAME).d("------> 服务已启动")
        createForegroundNotification()
        return START_REDELIVER_INTENT
    }

    // 创建前台服务常驻通知
    private fun createForegroundNotification() {
        val notification = buildNotification(
            true,
            FOREGROUND_CHANNEL_ID,
            FOREGROUND_CHANNEL_NAME,
            FOREGROUND_CONTENT_TITLE
        )
        startForeground(FOREGROUND_NOTIFICATION_ID, notification)
    }

    /**
     * 接收每个启动请求的 Intent
     */
    override fun onHandleIntent(intent: Intent?) {
        Timber.tag(SERVICE_NAME).d("------> 接收每个启动请求的 Intent")
    }

    /**
     * 当不再使用服务且准备将其销毁时，系统会调用此方法。
     * 服务应通过实现此方法来清理任何资源，如线程、注册的侦听器、接收器等。
     * 这是服务接收的最后一个调用。
     */
    override fun onDestroy() {
        Timber.tag(SERVICE_NAME).d("------> 服务已销毁")
        super.onDestroy()
        // 取消注册
        EventBus.getDefault().unregister(this)
        // 关闭长链接
        closeWebSocket()
        // 释放资源
        mDisposable?.dispose()
        mDisposable = null
    }

    /**
     * 创建通知
     */
    private fun buildNotification(
        isForeground: Boolean,
        channelId: String,
        channelName: String,
        contentTitle: String,
        contentText: String? = null,
        intent: PendingIntent? = null
    ): Notification {
        // 创建通知渠道
        createNotificationChannel(isForeground, channelId, channelName)
        // 创建通知
        val notification = NotificationCompat.Builder(this, channelId)
            .apply {
                setSmallIcon(R.drawable.ic_notifications)
                setContentTitle(contentTitle)
                if (contentText != null) {
                    setContentText(contentText)
                }
                if (intent != null) {
                    setContentIntent(intent)
                }
                setSilent(isForeground)
                if (!isForeground) {
                    priority = NotificationCompat.PRIORITY_DEFAULT
                    setAutoCancel(true)
                }
            }.build()
        return notification
    }

    /**
     * 创建通知渠道
     */
    private fun createNotificationChannel(
        isForeground: Boolean,
        channelId: String,
        channelName: String
    ) {
        // 8.0 以上需要特殊处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = if (isForeground) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     *
     */
    open fun onOpen(context: Context, webSocket: WebSocket, response: Response) {
        Timber.tag(SERVICE_NAME).d("------> 成功建立长链接")
    }

    /**
     *
     */
    open fun onMessage(context: Context, webSocket: WebSocket, text: String) {
        Timber.tag(SERVICE_NAME).d("------> 收到服务端消息：$text")
        try {
            val jsonObject = JsonParser.parseString(text).asJsonObject
            val modelName = jsonObject.get("modelName").asString
            val functionName = jsonObject.get("functionName").asString
            val params = jsonObject.get("params").asJsonObjectOrNull
            if (params == null) {
                Timber.tag(SERVICE_NAME).e("------> params is null")
            } else {
                when (FunctionEnum.fromName(functionName)) {
                    // 消息中心
                    FunctionEnum.MESSAGE -> {
                        // 发送通知
                        val title = params.get("title").asStringOrNull.orEmpty()
                        val message = params.get("content").asStringOrNull.orEmpty()
                        sendNotification(title, message)
                        // 刷新未读消息数
                        UnreadCountRefreshEvent.post()
                    }
                    else -> Timber.tag(SERVICE_NAME).e("functionName = $functionName，该类型未注册，请联系管理员。")
                }
            }
        } catch (e: UnsupportedOperationException) {
            Timber.tag(SERVICE_NAME).e(e)
        }
    }

    // 创建消息通知
    open fun sendNotification(title: String, message: String) {
        val intents = createIntents(this)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_ONE_SHOT
        val pendingIntent = PendingIntent.getActivities(this, 0, intents, flags)
        val notification = buildNotification(
            false,
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            title,
            message,
            pendingIntent
        )
        val id = UUID.randomUUID().hashCode()
        NotificationManagerCompat.from(this).notify(id, notification)
    }

    abstract fun createIntents(context: Context): Array<Intent>

    /**
     *
     */
    open fun onClosed(context: Context, webSocket: WebSocket, code: Int, reason: String) {
        Timber.tag(SERVICE_NAME).d("------> 连接关闭：code = $code，reason = $reason")
        canceled()
    }

    /**
     *
     */
    open fun onFailure(context: Context, webSocket: WebSocket, t: Throwable, response: Response?) {
        Timber.e(t, "------> 发生异常，5秒后重连")
        mDisposable = Single.timer(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _ ->
                newWebSocket()
            }
    }
}