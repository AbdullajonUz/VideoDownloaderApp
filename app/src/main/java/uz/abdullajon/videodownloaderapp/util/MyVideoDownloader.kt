package uz.abdullajon.videodownloaderapp.util

import android.content.Context
import android.os.Handler
import android.util.Log
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import uz.abdullajon.videodownloaderapp.data.model.VideoDownloadModel

class MyVideoDownloader(private val context: Context) {
    var downloaderId: Int? = null
    private var handler: Handler = Handler()

    private var videoDownloadModel = VideoDownloadModel(
        id = 0,
        title = "",
        url = "",
        downloadStatus = "",
        downloadFailedMessage = "",
        totalSizeTxt = "",
        downloadPercent = 0,
    )

    fun download(
        url: String,
        dirPath: String,
        vDownloadCallback: SingleBlock<VideoDownloadModel>
    ) {
        val index: Int = url.lastIndexOf("/")
        val fileName = url.substring(index + 1)
        Log.d("AAAAA", fileName)
        downloaderId = PRDownloader.download(
            url,
            dirPath,
            fileName
        ).build()
            .setOnStartOrResumeListener {
//                videoDownloadModel.downloadStatus = DOWNLOAD_STATUS_RESUME
//                vDownloadCallback.invoke(videoDownloadModel)
            }.setOnPauseListener {
//                handler.post {
//                    videoDownloadModel.downloadStatus = DOWNLOAD_STATUS_PAUSE
//                    vDownloadCallback.invoke(videoDownloadModel)
//                }
            }.setOnCancelListener {

            }.setOnProgressListener { percent ->
                videoDownloadModel.downloadPercent =
                    ((percent.currentBytes * 100) / percent.totalBytes).toInt()
                videoDownloadModel.downloadStatus = DOWNLOAD_STATUS_PROGRESS_UPDATE
                videoDownloadModel.downloadPercent =
                    (percent.currentBytes / percent.totalBytes).toInt()
                videoDownloadModel.id = downloaderId ?: 0
                val downloaderIdCurrent = downloaderId ?: 0
                Log.d("PPPPP", ((percent.currentBytes * 100) / percent.totalBytes).toString())

                val downloadModel = VideoDownloadModel(
                    downloaderIdCurrent,
                    fileName,
                    url,
                    DOWNLOAD_STATUS_PROGRESS_UPDATE,
                    "",
                    "",
                    ((percent.currentBytes * 100) / percent.totalBytes).toInt()
                )
                vDownloadCallback.invoke(downloadModel)
            }.start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    handler.post {
                        videoDownloadModel.url = url
                        videoDownloadModel.downloadStatus = DOWNLOAD_STATUS_COMPLETED
                        videoDownloadModel.title = fileName

                        vDownloadCallback.invoke(videoDownloadModel)
                    }
                }

                override fun onError(error: Error?) {
                    handler.post {
                        videoDownloadModel.downloadStatus = DOWNLOAD_STATUS_FAILED
                        videoDownloadModel.downloadFailedMessage = "Download Failed : $error"
                        vDownloadCallback.invoke(videoDownloadModel)
                    }
                }

            })
    }
}