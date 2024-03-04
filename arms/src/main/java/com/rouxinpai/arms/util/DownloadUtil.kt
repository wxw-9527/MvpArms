package com.rouxinpai.arms.util

import android.content.Context
import com.blankj.utilcode.util.PathUtils
import com.rouxinpai.arms.model.schedulersTransformer
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import timber.log.Timber
import java.io.File

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/1/3 17:03
 * desc   :
 */

class DownloadUtil {

    companion object {

        @Volatile
        private var instance: DownloadUtil? = null

        /**
         * 当前类单例对象
         */
        fun getInstance(): DownloadUtil =
            instance ?: synchronized(this) {
                instance ?: DownloadUtil().also { instance = it }
            }

        // 文件存储路径
        private val PATH = PathUtils.getExternalAppFilesPath() + File.separator + "download"
    }

    private var mRetrofit: Retrofit? = null

    private var mDisposable: Disposable? = null

    /**
     * 初始化
     */
    fun init(context: Context) {
        mRetrofit = EntryPointAccessors.fromApplication<NetworkProviderEntryPoint>(context)
            .retrofit()
    }

    /**
     * 开始下载
     */
    fun startDownload(
        url: String,
        fileName: String,
        listener: OnDownloadListener,
    ) {
        listener.onDownloadStart()
        mDisposable = mRetrofit?.create<DownloadService>()
            ?.downloadFile(url)
            ?.compose(schedulersTransformer())
            ?.subscribe(
                { responseBody ->
                    // 创建一个文件
                    val file = File(PATH + File.separator + fileName)
                    val writtenToDisk = writeResponseBodyToDisk(responseBody, file)
                    Timber.tag("DownloadUtil").d("File download was a success? $writtenToDisk")
                    if (writtenToDisk && file.isFile && file.exists()) {
                        listener.onDownloadComplete(file)
                    } else {
                        listener.onDownloadFail(Exception("文件下载失败"))
                    }
                },
                { e ->
                    Timber.tag("DownloadUtil").e(e)
                    listener.onDownloadFail(Exception(e))
                }
            )
    }

    private fun writeResponseBodyToDisk(body: ResponseBody, file: File): Boolean {
        return try {
            // 使用OKio的sink和buffer方法来写文件
            file.sink().buffer().use { sink ->
                sink.writeAll(body.source())
            }
            true
        } catch (e: Exception) {
            Timber.tag("DownloadUtil").e(e)
            false
        }
    }

    fun onDestroy() {
        //
        mDisposable?.dispose()
        mDisposable = null
    }

    interface OnDownloadListener {
        fun onDownloadStart()
        fun onDownloadFail(e: Exception?)
        fun onDownloadComplete(file: File)
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface NetworkProviderEntryPoint {
        fun retrofit(): Retrofit
    }

    interface DownloadService {

        @Streaming
        @GET
        fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>
    }
}