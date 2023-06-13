package com.rouxinpai.arms.di

import com.rouxinpai.arms.domain.util.DomainUtils
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/12/5 13:40
 * desc   :
 */
class AuthInterceptor @Inject constructor() : Interceptor {

    companion object {

        //
        private const val HEADER_NAME = "Authorization"

        //
        private const val HEADER_VALUE = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder().apply {
            val accessToken = DomainUtils.getAccessToken()
            if (!accessToken.isNullOrEmpty()) {
                val value = "$HEADER_VALUE $accessToken"
                Timber.d("$HEADER_NAME = $value")
                addHeader(HEADER_NAME, value)
            }
        }.build()
        return chain.proceed(request)
    }
}