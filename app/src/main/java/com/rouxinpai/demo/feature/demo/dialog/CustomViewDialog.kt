package com.rouxinpai.demo.feature.demo.dialog

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.View
import androidx.core.os.postDelayed
import androidx.lifecycle.LifecycleOwner
import com.kongzue.dialogx.dialogs.BottomDialog
import com.rouxinpai.arms.base.dialog.BaseMvpOnBindView
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.base.presenter.IPresenter
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.demo.databinding.CustomViewDialogBinding
import com.rouxinpai.demo.model.entity.login.CaptchaDTO
import com.rouxinpai.demo.model.remote.Api
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/16 16:59
 * desc   :
 */
class CustomViewDialog :
    BaseMvpOnBindView<BottomDialog, CustomViewDialogBinding, CustomViewContract.View, CustomViewPresenter>(), CustomViewContract.View {

    companion object {

        /**
         * 展示简单自定义弹窗
         */
        fun showBottomDialog() {
            BottomDialog.build()
                .setCustomView(CustomViewDialog())
                .show()
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface CustomViewPresenterEntryPoint {
        fun presenter(): CustomViewPresenter
    }

    override val stateLayout: View
        get() = binding.stateLayout

    override fun onCreatePresenter(activity: Activity): CustomViewPresenter {
        val accessors = EntryPointAccessors.fromApplication<CustomViewPresenterEntryPoint>(activity)
        return accessors.presenter()
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        presenter.getCaptchaImage()
        dialog.setOkButton("确认") { _, _ ->
            false
        }
    }

    override fun showCaptcha(bitmap: Bitmap) {
        binding.ivImage.setImageBitmap(bitmap)
    }
}

sealed interface CustomViewContract {

    interface View : IView {
        fun showCaptcha(bitmap: Bitmap)
    }

    interface Presenter : IPresenter<View> {
        fun getCaptchaImage()
    }
}

class CustomViewPresenter @Inject constructor() : BasePresenter<CustomViewContract.View>(),
    CustomViewContract.Presenter {

    override fun getCaptchaImage() {
        view?.showLoadingPage()
        val disposable = retrofit.create<Api>()
            .getCaptcha("${DomainUtils.getDomain()}code")
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .subscribeWith(object : DefaultObserver<CaptchaDTO>(view) {

                override fun onData(t: CaptchaDTO) {
                    super.onData(t)
                    val byteArray = Base64.decode(t.img, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    view?.showCaptcha(bitmap)
                    Handler(Looper.getMainLooper()).postDelayed(3000L) {
                        view?.showSuccessPage()
                    }
                }
            })
        addDisposable(disposable)
    }
}