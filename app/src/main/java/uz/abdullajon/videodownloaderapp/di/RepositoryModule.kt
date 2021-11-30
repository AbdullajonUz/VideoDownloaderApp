package uz.abdullajon.videodownloaderapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.abdullajon.videodownloaderapp.repository.videoRepository.VideoRepository
import uz.abdullajon.videodownloaderapp.repository.videoRepository.VideoRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun getVideoRepository(videoRepository: VideoRepositoryImpl): VideoRepository

}