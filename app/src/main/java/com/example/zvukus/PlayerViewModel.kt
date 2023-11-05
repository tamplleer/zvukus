package com.example.zvukus

import android.graphics.Bitmap
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zvukus.di.coroutine.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus
import com.example.zvukus.di.coroutine.ZvukusDispatchers
import com.example.zvukus.services.AudioManagerService
import com.example.zvukus.services.AudioRecorder
import com.example.zvukus.services.AudioTrack
import com.example.zvukus.services.SharedService
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val fileRepository: FileRepository,
    private val audioRecorder: AudioRecorder,
    private val sharedService: SharedService,
    private val audioManagerService: AudioManagerService,
    @Dispatcher(ZvukusDispatchers.IO) ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val scope = viewModelScope.plus(ioDispatcher)

    private var recordFile: File? = null
    private var recordFileTrack: File? = null

    private val _selectedTrack = MutableStateFlow<String?>(null)
    val selectedTrackId = _selectedTrack.asStateFlow()

    private val _selectedTrackTime = MutableStateFlow<Int?>(null)
    val selectedTrackTime = _selectedTrackTime.asStateFlow()

    private val _selectedTrackPlay = MutableStateFlow(false)
    val selectedTrackPlay = _selectedTrackPlay.asStateFlow()

    private val _uiUpdate = MutableStateFlow(0)
    val uiUpdate = _uiUpdate.asStateFlow()

    private val _selectedTrackPlaying = MutableStateFlow<String>("")
    val selectedTrackPlaying = _selectedTrackPlaying.asStateFlow()


    fun recordMic() {
        recordFile = audioRecorder.start("test.mp3")
    }

    fun recordTrack() {
        recordFileTrack = audioRecorder.startRecordTrack("track.mp3")
    }

    fun stopRecord() {
        audioRecorder.stop()
        recordFile?.let {
            val id = UUID.randomUUID().toString()
            audioManagerService.addAudioFile(id, it)?.also { player ->
                val track =
                    AudioTrack(id, 1.0f, 3, 1, null, "Record", file = it)
                fileRepository.addNewTrackWithId(track, player)
                selectTrack(track)
            }
        }
    }

    fun shared() {
        sharedService.shared(recordFileTrack)
    }


    fun playAll() {
        audioManagerService.startAll(listTrack.value.values.toList(), scope)
        _selectedTrackPlay.update { true }//todo think how to do
        _selectedTrackPlaying.update { "all" }
    }

    fun playTrack(track: AudioTrack) {
        audioManagerService.startOne(track, listTrack.value.values.toList(), scope)
        _selectedTrackPlay.update { true }
        _selectedTrackPlaying.update { track.id }
    }

    fun getTrackById(id: String?): AudioTrack? = id?.let {
        fileRepository.getTrackById(it)
    }


    fun changeVolume(volume: Float) {
        getTrackById(selectedTrackId.value)?.let { track ->
            audioManagerService.changeVolume(track, volume)?.let {
                fileRepository.changeVolume(track.id, it, volume)
            }
        }
    }

    fun changeIntervalTime(volume: Float) {
        getTrackById(selectedTrackId.value)?.let { track ->
            audioManagerService.changeIntervalTime(track, volume, scope)?.let {
                fileRepository.changeInterval(track.id, it, volume)
            }
        }
    }

    fun mute(track: AudioTrack, mute: Boolean) {
        audioManagerService.changeVolume(track, if (mute) 0f else track.volume)?.let {
            fileRepository.changeMute(track.id, mute, it)
            _uiUpdate.update { v -> v + 1 }
        }
    }

    fun stopAll() {
        audioManagerService.stopAll(listTrack.value.values.toList())
        _selectedTrackPlay.update { false }
        _selectedTrackPlaying.update { "" }
    }

    fun stopTrack(id: String, mediaPlayer: MediaPlayer?) {
        audioManagerService.stopTrack(id, mediaPlayer)
        _selectedTrackPlay.update { false }
        _selectedTrackPlaying.update { "" }
    }

    fun addTrack(track: AudioTrack) {
        audioManagerService.addAudio(track)?.also {
            fileRepository.addNewTrackWithId(track, it)
            selectTrack(track)
        }

    }

    fun selectTrack(track: AudioTrack) {
        _selectedTrack.update { track.id }
        _selectedTrackTime.update { track.mediaPlayer?.duration }

    }

    fun removeTrack(track: AudioTrack) {
        audioManagerService.stopAndReleaseTrack(track.mediaPlayer)
        fileRepository.removeTrack(track.id)
    }

    val listTrack =
        fileRepository.getTrackList().stateIn(scope, SharingStarted.Eagerly, emptyMap())
            .apply {
                onEach { Log.i("aa", "Each ${it.size}") }.launchIn(scope)
            }
    val listTrackSize =
        fileRepository.getTracksListSize().stateIn(scope, SharingStarted.Eagerly, 0).apply {
            onEach { Log.i("aa", "Each111") }.launchIn(scope)
        }


    private val _showLayer = MutableStateFlow(false)

    val showLayer = _showLayer.asStateFlow()

    fun showCloseLayer() {
        _showLayer.update { !it }
    }

}
