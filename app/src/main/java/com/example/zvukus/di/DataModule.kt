package com.example.zvukus.di

import com.example.zvukus.repository.TrackRepository
import com.example.zvukus.repository.TrackRepositoryCollections
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    @Singleton
    fun bindsFileRepository(
        trackRepository: TrackRepositoryCollections
    ): TrackRepository
}
