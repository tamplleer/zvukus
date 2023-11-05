package com.example.zvukus.services

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration


interface IAudioPlayer {

}

fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
    delay(initialDelay)
    while (true) {
        emit(Unit)
        delay(period)
    }
}

data class AudioTrack(
    val id: String,
    var volume: Float,
    val loop: Int,
    val priority: Int = 1,
    var intervalTime: Float? = null,
    val name: String,
    var mediaPlayer: MediaPlayer? = null,
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

/*
class AudioPlayer(val context: Context, val listTrack: List<AudioTrack>) {
    val soundPool = SoundPool.Builder()
        .setMaxStreams(10) //todo Максимальное количество одновременных потоков (сколько??)
        .build()

    lateinit var soundIds: List<Int>
    lateinit var streamIds: List<Int>
    val soundId1 = soundPool.load(context, R.raw.longsad, 1)
    val soundId2 = soundPool.load(context, R.raw.lowkik, 1)
    var streamId1 by Delegates.notNull<Int>()
    var streamId2 by Delegates.notNull<Int>()


    // Воспроизвести звуки
    fun loadAll() {
        soundIds = listTrack.map { soundPool.load(context, it.id, it.priority) }
        Log.i("aa", "soundIds =$soundIds")
    }

    fun start() {
        streamId1 = soundPool.play(soundId1, 1.0f, 1.0f, 1, 1, 0.5f)
        streamId2 = soundPool.play(soundId2, 1.0f, 1.0f, 1, 2, 2.0f)
        Log.i("aa", "$streamId1 $streamId2")
    }

    fun startAll() {
        listTrack.forEachIndexed { index, v ->
            soundPool.play(index+1, v.volume, v.volume, v.priority, v.loop, v.rate)
            Log.i("aa", "${index+1}")
        }
    }

    fun stopAll() {
        listTrack.forEachIndexed { index, v ->
            soundPool.stop(index+1)
            Log.i("aa", "stop = ${index+1}")
        }
        soundPool.release()
    }

    fun stop() {
        soundPool.stop(streamId1)
        soundPool.stop(streamId2)
        soundPool.release()
    }
}
*/

interface AudioManagerService {
    fun addAudio(track: AudioTrack): MediaPlayer?
    fun addAudioFile(id: String, file: File?): MediaPlayer?

    fun startAll(tracks: List<AudioTrack>, coroutineScope: CoroutineScope)
    fun startOne(track: AudioTrack, tracks: List<AudioTrack>, scope: CoroutineScope)
    fun stopAll(tracks: List<AudioTrack>)

    fun changeVolume(track: AudioTrack, value: Float): MediaPlayer?
    fun changeIntervalTime(
        track: AudioTrack,
        value: Float,
        coroutineScope: CoroutineScope
    ): MediaPlayer?

    fun stopTrack(id: String, mediaPlayer: MediaPlayer?)
    fun stopAndReleaseTrack(mediaPlayer: MediaPlayer?)
}

class AudioManagerServiceMediaPlayer @Inject constructor(@ApplicationContext val context: Context) :
    AudioManagerService {

    private val players = mutableMapOf<String, String>()// todo вынести во вью модел
    //  private val playersRun = MutableStateFlow(players.toMap())

    override fun addAudio(track: AudioTrack): MediaPlayer? {
        return if (track.resourceId == null) {//todo add toast
            null
        } else {
            players[track.id] = UUID.randomUUID().toString()
            MediaPlayer.create(context, track.resourceId).apply {
                isLooping = true
                setVolume(track.volume, track.volume)
                //   playbackParams = playbackParams.setSpeed(track.rate)
            }
        }
    }

    override fun addAudioFile(id: String, file: File?): MediaPlayer? {
        return if (file != null) {
            players[id] = UUID.randomUUID().toString()
            MediaPlayer.create(context, file.toUri())
        } else {
            null
        }
    }

    override fun startAll(tracks: List<AudioTrack>, coroutineScope: CoroutineScope) {
        stopAll(tracks)
        for (track in tracks) {
            players[track.id] = UUID.randomUUID().toString()
            track.mediaPlayer?.apply {
                prepareAsync()
                setOnPreparedListener {
                    start()
                    track.intervalTime?.let {
                        coroutineScope.launch {
                            val streamId = players[track.id]
                            while (players[track.id] == streamId) {
                                delay(duration.toLong())
                                if (players[track.id] == streamId) {
                                    pause()
                                    Log.i("aa", "111 ${isPlaying}")
                                    delay((it * 1000).toLong())
                                }


                                Log.i("aa", "st ${players[track.id] == streamId}")
                                if (players[track.id] == streamId) {
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
    }

    override fun startOne(
        track: AudioTrack,
        tracks: List<AudioTrack>,
        coroutineScope: CoroutineScope
    ) {
        stopAll(tracks)
        track.mediaPlayer?.apply {
            players[track.id] = UUID.randomUUID().toString()
            prepareAsync()
            setOnPreparedListener {
                start()
                Log.i("aa", "111 == ${duration}")
                track.intervalTime?.let {
                    coroutineScope.launch {
                        val streamId = players[track.id]
                        while (players[track.id] == streamId) {
                            delay(duration.toLong())
                            if (players[track.id] == streamId) {
                                pause()
                                delay((it * 1000).toLong())
                            }


                            Log.i("aa", "st ${players[track.id] == streamId}")
                            if (players[track.id] == streamId) {
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
            players[track.id] = UUID.randomUUID().toString()
            track.mediaPlayer?.apply {
                stop()//todo restart
            }
            // player.reset() todo mayme chtoto sdelat???/
            // player.release()
        }
        //players.clear()
    }

    override fun stopTrack(id: String, mediaPlayer: MediaPlayer?) {
        mediaPlayer?.apply {
            players[id] = UUID.randomUUID().toString()
            stop()
        }
    }

    override fun stopAndReleaseTrack(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
                release()
            }
            //todo remove trakRun
        }
    }

    override fun changeVolume(track: AudioTrack, value: Float): MediaPlayer? =
        track.mediaPlayer?.apply {
            setVolume(value, value)
        }

    override fun changeIntervalTime(
        track: AudioTrack,
        value: Float,
        coroutineScope: CoroutineScope
    ): MediaPlayer? {
        if (track.mediaPlayer?.isPlaying == true) {
            stopTrack(track.id, track.mediaPlayer)
            return track.mediaPlayer?.apply {
                players[track.id] = UUID.randomUUID().toString()
                prepareAsync()
                setOnPreparedListener {
                    start()
                    value.let {
                        coroutineScope.launch {
                            val streamId = players[track.id]
                            while (players[track.id] == streamId) {
                                delay(duration.toLong())
                                if (players[track.id] == streamId) {
                                    pause()
                                    Log.i("aa", "vremy =  ${it}")
                                    delay((it * 1000).toLong())
                                }


                                Log.i("aa", "st ${players[track.id] == streamId}")
                                if (players[track.id] == streamId) {
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
        return track.mediaPlayer
    }

}
