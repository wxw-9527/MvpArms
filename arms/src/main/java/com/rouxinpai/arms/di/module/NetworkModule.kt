package com.rouxinpai.arms.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rouxinpai.arms.base.application.BaseApplication
import com.rouxinpai.arms.di.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/13 10:19
 * desc   :
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLogInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { if (it.isNotBlank()) Timber.d(it) }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val application = context as BaseApplication
        val heartbeatInterval = application.heartbeatInterval
        val requestTimeout = application.requestTimeout
        return OkHttpClient.Builder()
            .pingInterval(heartbeatInterval, TimeUnit.SECONDS)
            .connectTimeout(requestTimeout, TimeUnit.SECONDS)
            .readTimeout(requestTimeout, TimeUnit.SECONDS)
            .writeTimeout(requestTimeout, TimeUnit.SECONDS)
            .addNetworkInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .serializeNulls()
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://www.domain.com/")
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}