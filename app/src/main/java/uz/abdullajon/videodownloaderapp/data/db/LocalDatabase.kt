package uz.abdullajon.videodownloaderapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.abdullajon.videodownloaderapp.data.db.dao.VideoDao
import uz.abdullajon.videodownloaderapp.data.model.VideoModel
/**
 *ikkinchi yo'l uchun yozilgan avval download bo'lganlarni ROOM bazaga saqlab
 * undan olmochi edim. Keyin filedan o'qib olindi
 */
@Database(
    version = 1,
    exportSchema = false,
    entities = [VideoModel::class]
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

}