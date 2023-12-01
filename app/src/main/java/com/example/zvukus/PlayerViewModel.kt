package com.example.zvukus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zvukus.di.coroutine.Dispatcher
import com.example.zvukus.di.coroutine.ZvukusDispatchers
import com.example.zvukus.model.AudioTrack
import com.example.zvukus.services.AudioManagerService
import com.example.zvukus.services.AudioRecorder
import com.example.zvukus.services.SharedService
import com.example.zvukus.services.TrackService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus
import java.io.File
import javax.inject.Inject


const val ALL_TRACK_SELECTED = "all"

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val trackService: TrackService,
    private val audioRecorder: AudioRecorder,
    private val sharedService: SharedService,
    private val audioManagerService: AudioManagerService,
    @Dispatcher(ZvukusDispatchers.IO) ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val scope = viewModelScope.plus(ioDispatcher)

    private var recordFile: File? = null

    private val _selectedTrack = MutableStateFlow<String?>(null)
    val selectedTrackId = _selectedTrack.asStateFlow()

    private val _selectedTrackTime = MutableStateFlow<Int?>(null)
    val selectedTrackTime = _selectedTrackTime.asStateFlow()

    private val _selectedTrackPlay = MutableStateFlow(false)
    val selectedTrackPlay = _selectedTrackPlay.asStateFlow()

    private val _selectedTrackPlaying = MutableStateFlow("")
    val selectedTrackPlaying = _selectedTrackPlaying.asStateFlow()

    private val _selectedRecord = MutableStateFlow("")
    val selectedRecord = _selectedRecord.asStateFlow()

    val listTrack =
        trackService.getTrackList().stateIn(scope, SharingStarted.Eagerly, emptyList())


    private val _showLayer = MutableStateFlow(false)

    val showLayer = _showLayer.asStateFlow()

    fun showCloseLayer() {
        _showLayer.update { !it }
    }

    fun setRecordId(id: String) {
        _selectedRecord.update { id }
    }


    fun recordMic() {
        recordFile = audioRecorder.start("record ${listTrack.value.size + 1}.mp3")
    }

    fun recordTrack() {
        recordFile = audioRecorder.startRecordTrack("track.mp3")
    }

    fun stopRecordTrack() {
        audioRecorder.stop()
    }

    fun stopRecord() {
        audioRecorder.stop()
        recordFile?.let {
            audioManagerService.addAudioFile(it)?.also { player ->
                val track = AudioTrack.defaultTrack()
                    .copy(
                        file = it,
                        mediaPlayer = player,
                        name = recordFile?.name ?: "record",
                        intervalTime = null
                    )

                selectTrack(trackService.addTrack(track))
            }
        }
    }

    fun shared() {
        sharedService.shared(recordFile)
    }


    fun playAll() {
        _selectedTrackTime.update {
            listTrack.value.maxByOrNull {
                it.mediaPlayer?.mediaPlayer?.duration ?: 0
            }?.mediaPlayer?.mediaPlayer?.duration
        }
        audioManagerService.startAll(listTrack.value, scope)
        _selectedTrackPlay.update { true }
        _selectedTrackPlaying.update { ALL_TRACK_SELECTED }

    }

    fun playTrack(track: AudioTrack) {
        selectTrack(track)
        audioManagerService.startOne(track, listTrack.value, scope)
        _selectedTrackPlay.update { true }
        _selectedTrackPlaying.update { track.id }
    }

    fun getTrackById(id: String?): AudioTrack? = id?.let {
        trackService.getTrackById(it)
    }


    fun changeVolume(volume: Float) {
        getTrackById(selectedTrackId.value)?.let { track ->
            audioManagerService.changeVolume(track, volume)?.let {
                trackService.changeVolume(track.id, it, volume)
            }
        }
    }

    fun changeIntervalTime(volume: Float) {
        getTrackById(selectedTrackId.value)?.let { track ->
            audioManagerService.changeIntervalTime(track, volume, scope)?.let {
                trackService.changeInterval(track.id, it, volume)
            }
        }
    }

    fun mute(track: AudioTrack, mute: Boolean) {
        audioManagerService.changeVolume(track, if (mute) 0f else track.volume)?.let {
            trackService.changeMute(track.id, mute, it)
        }
    }

    fun stopAll() {
        audioManagerService.stopAll(listTrack.value)
        _selectedTrackPlay.update { false }
        _selectedTrackPlaying.update { "" }
    }

    fun stopTrack(id: String, mediaPlayer: TrackMediaPlayer?) {
        audioManagerService.stopTrack(id, mediaPlayer)
        _selectedTrackPlay.update { false }
        _selectedTrackPlaying.update { "" }
    }

    fun addTrack(track: AudioTrack) {
        audioManagerService.addAudio(track)?.also {
            selectTrack(trackService.addTrack(track.apply { mediaPlayer = it }))
            _showLayer.update { true }
        }

    }

    fun selectTrack(track: AudioTrack) {
        _selectedTrack.update { track.id }
        _selectedTrackTime.update { track.mediaPlayer?.mediaPlayer?.duration }
    }

    fun removeTrack(track: AudioTrack) {
        audioManagerService.stopAndReleaseTrack(track.mediaPlayer, track.id)
        trackService.removeTrack(track.id)
    }


    fun pause() {
        stopAll()
    }

    fun resume() {
    }

}
