package uz.abdullajon.videodownloaderapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.abdullajon.videodownloaderapp.data.db.LocalDatabase
import uz.abdullajon.videodownloaderapp.data.db.dao.VideoDao
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {


    @Provides
    @Singleton
    fun getLocalDatabase(
        @ApplicationContext context: Context,
    ): LocalDatabase =
        Room.databaseBuilder(context, LocalDatabase::class.java, "MyLocal.db")
            .allowMainThreadQueries()
            .build()

    @Provides
    @Singleton
    fun getVideoDao(
        localDatabase: LocalDatabase
    ): VideoDao = localDatabase.videoDao()

}