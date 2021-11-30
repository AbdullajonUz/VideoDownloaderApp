package uz.abdullajon.videodownloaderapp.data.model

data class VideoDownloadModel(
    var id: Int,
    var title: String,
    var url: String,
    var downloadStatus: String,
    var downloadFailedMessage: String,
    var totalSizeTxt: String,
    var downloadPercent: Int,
)