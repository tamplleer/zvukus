package com.example.zvukus.services

import android.content.Context
import com.example.zvukus.TrackMediaPlayer
import com.example.zvukus.model.AudioTrack
import com.example.zvukus.repository.TrackRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface TrackService {
    fun getTrackList(): Flow<List<AudioTrack>>
    fun addTrack(track: AudioTrack): AudioTrack
    fun removeTrack(id: String)
    fun changeMute(id: String, mute: Boolean, player: TrackMediaPlayer)
    fun changeVolume(id: String, player: TrackMediaPlayer, volume: Float = 0.5f)
    fun changeInterval(id: String, player: TrackMediaPlayer, interval: Float = 1f)
    fun getTrackById(id: String): AudioTrack?
    fun setSelectedTrack(track: AudioTrack)
    fun getSelectTrack(): Flow<AudioTrack?>
}

class TrackDefaultService @Inject constructor(
    @ApplicationContext val context: Context,
    private val trackRepository: TrackRepository
) : TrackService {
    override fun getTrackList(): Flow<List<AudioTrack>> =
        trackRepository.getTrackList().map { map ->
            map.values.toList().sortedBy { it.id }

        }

    override fun addTrack(track: AudioTrack): AudioTrack {
        return trackRepository.addTrack(track)
    }

    override fun removeTrack(id: String) {
        trackRepository.removeTrack(id)
    }

    override fun changeMute(id: String, isMute: Boolean, player: TrackMediaPlayer) {
        getTrackById(id)?.apply {
            mute = isMute
            mediaPlayer = player
        }?.let { trackRepository.updateTrack(it) }

    }

    override fun changeVolume(id: String, player: TrackMediaPlayer, vol: Float) {
        getTrackById(id)?.apply {
            mediaPlayer = player
            volume = vol
        }?.let { trackRepository.updateTrack(it) }
    }

    override fun changeInterval(id: String, player: TrackMediaPlayer, interval: Float) {
        getTrackById(id)?.apply {
            mediaPlayer = player
            intervalTime = interval
        }?.let { trackRepository.updateTrack(it) }
    }

    override fun getTrackById(id: String): AudioTrack? = trackRepository.getTrackById(id)
    override fun setSelectedTrack(track: AudioTrack) {
        trackRepository.setSelectedTrack(track)
    }

    override fun getSelectTrack(): Flow<AudioTrack?> = trackRepository.getSelectTrack()


}
