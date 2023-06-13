package com.rouxinpai.demo.main.login

import android.graphics.BitmapFactory
import android.util.Base64
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.EncryptUtils
import com.google.gson.JsonObject
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.extension.toRequestBody
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.domain.util.DomainUtils
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.demo.http.Api
import com.rouxinpai.demo.model.AccessTokenDTO
import com.rouxinpai.demo.model.CaptchaDTO
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/9 9:19
 * desc   :
 */
class AccountLoginPresenter @Inject constructor() : BasePresenter<AccountLoginContract.View>(),
    AccountLoginContract.Presenter {

    companion object {
        // 公钥
        const val KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo5Cxzb10Z0tevNvnRhbW" +
                "u1MBcStpYhKuXwzQ5kr4FT8RAYk0+OBQxFqEyb10n5WBu2zAG6AKw6uRmjVK5/FK" +
                "if2LN6LgFYiJSwBOrwpTu1hUHO8Enu1rg1QkdJ6TrreIWwOJd8CZYP94Orfcs0QB" +
                "okFiRx8kDBLFTmNVIEZtomgs3cVxVZjrzbGKkzQGf9CVoDXDGYm4/URT9y9s4dnC" +
                "rAmOqOsjRYlcxTDa0yB2Q9hVpi++R6VnMcTodYGr9WYwh8ndSY/ocT+Jn+hoj+AH" +
                "8rne/vQ+0Pj4G1BUCk6BB2CPjMTOmqxJ1h8w/reg4Y/aik1uVajhRjB287txY/lo" +
                "oQIDAQAB"
        const val SIZE = 1024
        const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"
    }

    //
    private lateinit var mUuid: String

    override fun getCaptcha() {
        val disposable = retrofit.create<Api>()
            .getCaptcha("${DomainUtils.getDomain()}code")
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .subscribeWith(object : DefaultObserver<CaptchaDTO>(view) {

                override fun onData(t: CaptchaDTO) {
                    super.onData(t)
                    mUuid = t.uuid
                    val byteArray = Base64.decode(t.img, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    view?.showCaptcha(bitmap)
                }
            })
        addDisposable(disposable)
    }

    override fun login(account: String, password: String, captcha: String, rememberMe: Boolean) {
        view?.showProgress()
        val data = password.toByteArray()
        val publishKey = EncodeUtils.base64Decode(KEY)
        val rsa = EncryptUtils.encryptRSA(data, publishKey, SIZE, TRANSFORMATION)
        val encPassword = EncodeUtils.base64Encode2String(rsa)
        val jsonObject = JsonObject().apply {
            addProperty("uuid", mUuid)
            addProperty("account", account)
            addProperty("password", encPassword)
            addProperty("code", captcha)
        }
        val requestBody = jsonObject.toRequestBody()
        val disposable = retrofit.create<Api>()
            .accountLogin("${DomainUtils.getDomain()}auth/login", requestBody)
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .subscribeWith(object : DefaultObserver<AccessTokenDTO>(view) {

                override fun onData(t: AccessTokenDTO) {
                    super.onData(t)
                    // 缓存token
                    DomainUtils.setAccessToken(t.accessToken)
                    // 登录成功
                    view?.dismissProgress()
                    view?.loginSuccessful()
                }

                override fun onFail(e: Throwable) {
                    super.onFail(e)
                    getCaptcha()
                    view?.loginFailed()
                }
            })
        addDisposable(disposable)
    }
}