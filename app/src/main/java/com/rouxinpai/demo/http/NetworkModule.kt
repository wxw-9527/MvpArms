package com.rouxinpai.demo.http

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
 * time   : 2022/11/28 10:19
 * desc   :
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //
    private const val TOKEN =
        "eyJhbGciOiJIUzUxMiJ9.eyJ3eF9hcHBfc2Vzc2lvbl9rZXkiOm51bGwsImFkbWluX2ZsYWciOjEsInVzZXJfaWQiOjE2MzE0ODQ1MzkwNDA3OTY2NzMsInl1bl9jb25zb2xlX3Rva2VuIjpudWxsLCJ3eF9hcHBfb3BlbmlkIjpudWxsLCJ1c2VyX2tleSI6IjM0Mzc0MDUxLWE5OTgtNGE1YS05YWZiLWFkMzQ4YmVjMjVmYSIsImRlcHRfaWQiOjE2MzE0Nzk0MjEwNjI5NzU0OTAsImN1c3RvbWVyX2lkIjoxNjMxNDc5NDIwNTYzODUzMzE0LCJ3eF91bmlvbl9pZCI6bnVsbCwidXNlcm5hbWUiOiJERmFkbWluIn0.fBQqNmKVmyV3D5JHkUf6bt6gEA0Ms1VHSJUrxDWysa6Pi1UVtj6Jvis9w-k3I7nESxyYv81IeFKseYaNWSm6-g"

    @Provides
    @Singleton
    fun provideLogInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { if (it.isNotBlank()) Timber.d(it) }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .addNetworkInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder().apply {
                    addHeader("Authorization", TOKEN)
                }.build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor).build()
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
            .baseUrl("http://192.168.118.160:55/stage-api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}