package com.rouxinpai.arms.message.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.rouxinpai.arms.R
import com.rouxinpai.arms.message.api.MessageApi
import com.rouxinpai.arms.message.feature.MessageListActivity
import com.rouxinpai.arms.message.model.UnreadCountRefreshEvent
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.arms.view.BGABadgeImageView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.ViewComponentManager.FragmentContextWrapper
import io.reactivex.rxjava3.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/21 14:42
 * desc   :
 */
@AndroidEntryPoint
class UnreadMessagesCountImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BGABadgeImageView(context, attrs, defStyleAttr), DefaultLifecycleObserver,
    OnClickListener {

    @Inject
    lateinit var retrofit: Retrofit

    /**
     * 获取关联的生命周期对象 [Lifecycle]。
     *
     * @return 关联的生命周期对象 [Lifecycle]，如果未找到则返回 null。
     */
    private val mLifecycle: Lifecycle?
        get() {
            val context = context
            if (context is AppCompatActivity) {
                return context.lifecycle
            } else if (context is FragmentContextWrapper) {
                val base = context.baseContext
                if (base is AppCompatActivity) {
                    return base.lifecycle
                }
            }
            return null
        }

    private var mDisposable: Disposable? = null

    init {
        // 设置默认图片资源
        setImageResource(R.drawable.ic_notifications)
        // 绑定监听事件
        setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mLifecycle?.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        EventBus.getDefault().register(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        refreshUnreadMessagesCount()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        //
        mLifecycle?.removeObserver(this)
        //
        EventBus.getDefault().unregister(this)
        //
        mDisposable?.dispose()
        mDisposable = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UnreadCountRefreshEvent) {
        refreshUnreadMessagesCount()
    }

    override fun onClick(v: View?) {
        MessageListActivity.start(context)
    }

    /**
     * 更新未读消息数
     */
    private fun refreshUnreadMessagesCount() {
        mDisposable = retrofit.create<MessageApi>()
            .getUnreadMessagesCount()
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .subscribeWith(object : DefaultObserver<Int>(null) {

                override fun onData(t: Int) {
                    super.onData(t)
                    when {
                        t in 1..99 -> showTextBadge(t.toString())
                        t > 99 -> showTextBadge("99+")
                        else -> hiddenBadge()
                    }
                }
            })
    }
}