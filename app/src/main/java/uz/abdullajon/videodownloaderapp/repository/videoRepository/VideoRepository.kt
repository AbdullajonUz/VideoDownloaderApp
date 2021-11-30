package uz.abdullajon.videodownloaderapp.repository.videoRepository

import kotlinx.coroutines.flow.Flow
import uz.abdullajon.videodownloaderapp.data.model.VideoModel

interface VideoRepository {

    fun getVideoList(): Flow<List<VideoModel>>

    fun addVideo(videoModel: VideoModel): Flow<Long>
}