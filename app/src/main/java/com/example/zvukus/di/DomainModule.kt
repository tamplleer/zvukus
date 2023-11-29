package com.example.zvukus.di

import com.example.zvukus.services.AudioManagerService
import com.example.zvukus.services.AudioManagerServiceMediaPlayer
import com.example.zvukus.services.AudioRecorder
import com.example.zvukus.services.AudioRecorderService
import com.example.zvukus.services.SharedAudio
import com.example.zvukus.services.SharedService
import com.example.zvukus.services.TrackDefaultService
import com.example.zvukus.services.TrackService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {
    @Binds
    @Singleton
    fun bindsAudioRecorderService(
        audioRecorderService: AudioRecorderService
    ): AudioRecorder

    @Binds
    @Singleton
    fun bindsSharedService(
        sharedService: SharedAudio
    ): SharedService

    @Binds
    @Singleton
    fun bindsAudioManagerService(
        audioManager: AudioManagerServiceMediaPlayer
    ): AudioManagerService
    @Binds
    @Singleton
    fun bindsTrackService(
        trackService: TrackDefaultService
    ): TrackService
}
