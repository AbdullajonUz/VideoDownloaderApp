package uz.abdullajon.videodownloaderapp.data.db.dao

import androidx.room.*
import uz.abdullajon.videodownloaderapp.data.model.VideoModel

@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVideo(videoItem: VideoModel): Long

    @Update
    suspend fun updateData(videoItem: VideoModel)

    @Query("SELECT * FROM video")
    fun getAllEvents(): List<VideoModel>

}