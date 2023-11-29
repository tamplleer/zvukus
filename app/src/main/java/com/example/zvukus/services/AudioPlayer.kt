package com.example.zvukus.services

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import com.example.zvukus.TrackMediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject


data class AudioTrack @Inject constructor(
    val id: String,
    var volume: Float,
    val loop: Int,//todo delete
    val priority: Int = 1,//todo delete
    var intervalTime: Float? = null,
    val name: String,
    var mediaPlayer: TrackMediaPlayer? = null,
    val resourceId: Int? = null,
    val file: File? = null,
    var soundId: Int = -1,
    var mute: Boolean = false,
) {
    companion object {
        fun defaultTrack(): AudioTrack =
            AudioTrack(UUID.randomUUID().toString(), 1.0f, 0, 1, 2f, "Unknown")
    }

}

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

    private val activeIdSessionMap = mutableMapOf<String, String>()// todo вынести во вью модел

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
                                    Log.i("aa", "111 ${isPlaying}")
                                    delay((it * 1000).toLong())
                                }


                                Log.i("aa", "st ${activeIdSessionMap[track.id] == streamId}")
                                if (activeIdSessionMap[track.id] == streamId) {
                                    start()
                                }
                            }
                        }
                    }
                }
                setOnCompletionListener {
                    Log.i("aa","STOP ${isPlaying}")
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
            Log.i("aa", "track = ${activeIdSessionMap.map { it }}")
            prepareAsync()
            setOnPreparedListener {
                start()
                Log.i("aa", "111 == ${duration}")
                track.intervalTime?.let {
                    coroutineScope.launch {
                        val streamId = activeIdSessionMap[track.id]
                        while (activeIdSessionMap[track.id] == streamId) {
                            delay(duration.toLong())
                            if (activeIdSessionMap[track.id] == streamId) {
                                pause()
                                delay((it * 1000).toLong())
                            }


                            Log.i("aa", "st ${activeIdSessionMap[track.id] == streamId}")
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
            //todo release when stop!!
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
                                    Log.i("aa", "vremy =  ${it}")
                                    delay((it * 1000).toLong())
                                }


                                Log.i("aa", "st ${activeIdSessionMap[track.id] == streamId}")
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
