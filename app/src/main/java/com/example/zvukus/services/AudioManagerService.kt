package com.example.zvukus.services

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import com.example.zvukus.TrackMediaPlayer
import com.example.zvukus.model.AudioTrack
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject




interface AudioManagerService {
    fun addAudio(track: AudioTrack): TrackMediaPlayer?
    fun addAudioFile(file: File?): TrackMediaPlayer?

    fun startAll(tracks: List<AudioTrack>, coroutineScope: CoroutineScope)
    fun startOne(track: AudioTrack, tracks: List<AudioTrack>, scope: CoroutineScope)
    fun stopAll(tracks: List<AudioTrack>)

    fun changeVolume(track: AudioTrack, value: Float): TrackMediaPlayer?
    fun changeIntervalTime(
        track: AudioTrack,
        value: Float,
        coroutineScope: CoroutineScope
    ): TrackMediaPlayer?

    fun stopTrack(id: String, mediaPlayer: TrackMediaPlayer?)
    fun stopAndReleaseTrack(mediaPlayer: TrackMediaPlayer?, trackId: String)
}

class AudioManagerServiceMediaPlayer @Inject constructor(@ApplicationContext val context: Context) :
    AudioManagerService {

    private val activeIdSessionMap = mutableMapOf<String, String>()

    override fun addAudio(track: AudioTrack): TrackMediaPlayer? {
        return if (track.resourceId == null) {
            null
        } else {
            TrackMediaPlayer(
                MediaPlayer.create(context, track.resourceId).apply {
                    isLooping = true
                    setVolume(track.volume, track.volume)
                })
        }
    }

    override fun addAudioFile(file: File?): TrackMediaPlayer? {
        return if (file != null) {
            TrackMediaPlayer(MediaPlayer.create(context, file.toUri()))
        } else {
            null
        }
    }

    override fun startAll(tracks: List<AudioTrack>, coroutineScope: CoroutineScope) {
        stopAll(tracks)
        for (track in tracks) {
            activeIdSessionMap[track.id] = UUID.randomUUID().toString()
            track.mediaPlayer?.mediaPlayer?.apply {
                prepareAsync()
                setOnPreparedListener {
                    start()
                    track.intervalTime?.let {
                        coroutineScope.launch {
                            val streamId = activeIdSessionMap[track.id]
                            while (activeIdSessionMap[track.id] == streamId) {
                                delay(duration.toLong())
                                if (activeIdSessionMap[track.id] == streamId) {
                                    pause()
                                    delay((it * 1000).toLong())
                                }


                                if (activeIdSessionMap[track.id] == streamId) {
                                    start()
                                }
                            }
                        }
                    }
                }
                setOnCompletionListener {
                    if (isPlaying){stop()}

                }
            }
        }
    }

    override fun startOne(
        track: AudioTrack,
        tracks: List<AudioTrack>,
        coroutineScope: CoroutineScope
    ) {
        stopAll(tracks)
        track.mediaPlayer?.mediaPlayer?.apply {
            activeIdSessionMap[track.id] = UUID.randomUUID().toString()
            prepareAsync()
            setOnPreparedListener {
                start()
                track.intervalTime?.let {
                    coroutineScope.launch {
                        val streamId = activeIdSessionMap[track.id]
                        while (activeIdSessionMap[track.id] == streamId) {
                            delay(duration.toLong())
                            if (activeIdSessionMap[track.id] == streamId) {
                                pause()
                                delay((it * 1000).toLong())
                            }


                            if (activeIdSessionMap[track.id] == streamId) {
                                start()
                            }
                        }

                    }
                }
            }
            setOnCompletionListener {
                stop()
            }
        }
    }

    override fun stopAll(tracks: List<AudioTrack>) {
        for (track in tracks) {
            activeIdSessionMap[track.id] = UUID.randomUUID().toString()
            track.mediaPlayer?.mediaPlayer?.apply {
                stop()
            }
        }
    }

    override fun stopTrack(id: String, mediaPlayer: TrackMediaPlayer?) {
        mediaPlayer?.mediaPlayer?.apply {
            activeIdSessionMap[id] = UUID.randomUUID().toString()
            stop()
        }
    }

    override fun stopAndReleaseTrack(mediaPlayer: TrackMediaPlayer?, trackId: String) {
        activeIdSessionMap.remove(trackId)
        mediaPlayer?.mediaPlayer?.apply {
            if (isPlaying) {
                stop()
                release()
            }
        }
    }

    override fun changeVolume(track: AudioTrack, value: Float): TrackMediaPlayer? =
        TrackMediaPlayer(track.mediaPlayer?.mediaPlayer?.apply {
            setVolume(value, value)
        })

    override fun changeIntervalTime(
        track: AudioTrack,
        value: Float,
        coroutineScope: CoroutineScope
    ): TrackMediaPlayer? {
        if (track.mediaPlayer?.mediaPlayer?.isPlaying == true) {
            stopTrack(track.id, track.mediaPlayer)
            return TrackMediaPlayer(track.mediaPlayer?.mediaPlayer?.apply {
                activeIdSessionMap[track.id] = UUID.randomUUID().toString()
                prepareAsync()
                setOnPreparedListener {
                    start()
                    value.let {
                        coroutineScope.launch {
                            val streamId = activeIdSessionMap[track.id]
                            while (activeIdSessionMap[track.id] == streamId) {
                                delay(duration.toLong())
                                if (activeIdSessionMap[track.id] == streamId) {
                                    pause()
                                    delay((it * 1000).toLong())
                                }
                                if (activeIdSessionMap[track.id] == streamId) {
                                    start()
                                }
                            }

                        }
                    }
                }
                setOnCompletionListener {
                    stop()
                }
            })
        }
        return track.mediaPlayer
    }

}
