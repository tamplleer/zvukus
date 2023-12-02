package com.example.zvukus.repository

import com.example.zvukus.model.AudioTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date
import javax.inject.Inject

interface TrackRepository {
    fun getTrackList(): Flow<Map<String, AudioTrack>>
    fun addTrack(track: AudioTrack): AudioTrack
    fun updateTrack(track: AudioTrack)
    fun removeTrack(id: String)
    fun getTrackById(id: String): AudioTrack?
    fun setSelectedTrack(track: AudioTrack)
    fun getSelectTrack(): Flow<AudioTrack?>
}

class TrackRepositoryCollections @Inject constructor() : TrackRepository {


    private val tracksData = mutableMapOf<String, AudioTrack>()
    private val tracks = MutableStateFlow(tracksData.toMap())


    private val selected = MutableStateFlow<AudioTrack?>(null)


    override fun getTrackList(): Flow<Map<String, AudioTrack>> {
        return tracks
    }


    override fun addTrack(track: AudioTrack): AudioTrack {
        val id = Date().time.toString()
        val resultTrack = track.copy(id = id)
        tracks.update {
            tracksData.putIfAbsent(
                id,
                resultTrack
            )
            HashMap(tracksData)
        }
        return resultTrack
    }

    override fun updateTrack(track: AudioTrack) {
        tracks.update {
            tracksData[track.id] = track
            HashMap(tracksData)
        }
    }

    override fun removeTrack(id: String) {
        tracks.update {
            tracksData.remove(id)
            HashMap(tracksData)
        }
    }

    override fun getTrackById(id: String): AudioTrack? = tracksData[id]
    override fun setSelectedTrack(track: AudioTrack) {
        selected.update { track }
    }

    override fun getSelectTrack(): Flow<AudioTrack?> {
        return selected
    }

}