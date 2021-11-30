package uz.abdullajon.videodownloaderapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = "video")
data class VideoModel(
    @PrimaryKey
    var localURL: String,
    var title: String,
    var totalSpace: String
) : Parcelable