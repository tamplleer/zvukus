package com.example.zvukus

import android.media.MediaPlayer
import com.example.zvukus.services.AudioTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

interface FileRepository {
    fun getTrackList(): Flow<Map<String, AudioTrack>>
    fun getTracksListSize(): Flow<Int>
    fun addNewTrackWithId(track: AudioTrack, player: MediaPlayer)
    fun removeTrack(id: String)
    fun changeMute(id: String, mute: Boolean, player: MediaPlayer)
    fun changeVolume(id: String, player: MediaPlayer, volume: Float = 0.5f)
    fun changeInterval(id: String, player: MediaPlayer, interval: Float = 1f)
    fun getTrackById(id: String): AudioTrack?
}

class FileRepositoryCollections @Inject constructor() : FileRepository {


    private val tracksData = mutableMapOf<String, AudioTrack>()
    private val tracks = MutableStateFlow(tracksData.toMap())
    private val tracksListSize = MutableStateFlow(0)


    override fun getTrackList(): Flow<Map<String, AudioTrack>> {
        return tracks
    }

    override fun getTracksListSize(): Flow<Int> = tracksListSize


    override fun addNewTrackWithId(track: AudioTrack, player: MediaPlayer) {

        tracks.update {
            val exist = tracksData.putIfAbsent(
                track.id,
                track.apply { mediaPlayer = player })
            if (exist != null) {
                val id = UUID.randomUUID().toString()
                tracksData[id] = AudioTrack(
                    id,
                    track.volume,
                    track.loop,
                    track.priority,
                    track.intervalTime,
                    "${track.name} ${tracks.value.size}",//todo podum increase
                    player,
                    track.resourceId,
                    track.file,
                    track.soundId,
                    track.mute
                )
            }
            tracksData
        }
        //   Log.i("aa",tr)
        tracksListSize.update { it + 1 }
    }

    override fun removeTrack(id: String) {
        tracks.update {
            tracksData.remove(id)
            tracksData
        }
        tracksListSize.update { it - 1 }
    }

    override fun changeMute(id: String, isMute: Boolean, player: MediaPlayer) {
        tracks.update {
            tracksData[id]?.also {
                tracksData[id] = it.apply {
                    mute = isMute
                    mediaPlayer = player
                }
            }

            tracksData
        }
    }

    override fun changeVolume(id: String, player: MediaPlayer, vol: Float) {
        tracks.update {
            tracksData[id]?.also {
                tracksData[id] = it.apply {
                    mediaPlayer = player
                    volume = vol
                }
            }

            tracksData
        }
    }

    override fun changeInterval(id: String, player: MediaPlayer, interval: Float) {
        tracks.update {
            tracksData[id]?.also {
                tracksData[id] = it.apply {
                    mediaPlayer = player
                    intervalTime = interval
                }
            }

            tracksData
        }
    }

    override fun getTrackById(id: String): AudioTrack? = tracksData[id]
}