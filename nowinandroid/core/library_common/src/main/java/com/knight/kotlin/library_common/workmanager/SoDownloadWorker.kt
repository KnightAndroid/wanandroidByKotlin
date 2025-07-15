package com.knight.kotlin.library_common.workmanager

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.knight.kotlin.library_common.util.BaiduSoDownloaderUtils
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/27 9:38
 * @descript:
 */
class SoDownloadWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val context = applicationContext
            val loaded = CompletableDeferred<Boolean>()

            BaiduSoDownloaderUtils.load(context, object : BaiduSoDownloaderUtils.OnSoLoadCallback {
                override fun onProgress(totalProgress: Int, text: String) {
                    // 可上报进度，也可使用 setProgress(...) 传递给 UI
                    // 这里把进度发给 WorkManager
                    setProgressAsync(workDataOf("progress" to totalProgress))
                }

                override fun onSuccess() {
                    loaded.complete(true)
                }

                override fun onFailure(e: Throwable) {
                    loaded.complete(false)
                }
            })

            val result = loaded.await()
            return@withContext if (result) Result.success() else Result.retry()  // 可设置失败重试
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.retry()
        }
    }
}