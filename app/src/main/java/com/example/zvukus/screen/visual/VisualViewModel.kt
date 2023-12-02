package com.example.zvukus.screen.visual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zvukus.di.coroutine.Dispatcher
import com.example.zvukus.di.coroutine.ZvukusDispatchers
import com.example.zvukus.model.AudioTrack
import com.example.zvukus.services.AudioManagerService
import com.example.zvukus.services.TrackService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus
import javax.inject.Inject


@HiltViewModel
class VisualViewModel @Inject constructor(
    private val trackService: TrackService,
    private val audioManagerService: AudioManagerService,
    @Dispatcher(ZvukusDispatchers.IO) ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val scope = viewModelScope.plus(ioDispatcher)


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

    val trackGlobal =
        trackService.getSelectTrack().stateIn(scope, SharingStarted.Eagerly, null)


    private val _isRunningTrack = MutableStateFlow(false)

    val isRunningTrack = _isRunningTrack.asStateFlow()


    fun playGlobalTrack() {
        _isRunningTrack.update { true }
        trackGlobal.value?.let { track ->
            _selectedTrackTime.update { track?.mediaPlayer?.mediaPlayer?.duration ?: 2000 }
            _selectedTrackPlay.update { true }
            audioManagerService.startOne(track, listTrack.value, scope)
            _selectedTrackPlaying.update { track.id }
        }
    }

    fun getTrackById(id: String?): AudioTrack? = id?.let {
        trackService.getTrackById(it)
    }


    fun stopAll() {
        audioManagerService.stopAll(listTrack.value)
        _selectedTrackPlay.update { false }
        _selectedTrackPlaying.update { "" }
    }

    fun stopTrackGlobal() {
        _isRunningTrack.update { false }
        trackGlobal.value?.let {
            val media = audioManagerService.stopTrack(it.id, it.mediaPlayer)
            media?.let { mediaValue ->
                trackGlobal.value?.let { track ->
                    trackService.setSelectedTrack(track.copy(mediaPlayer = mediaValue))
                }
            }
        }
    }

    fun addTrack(track: AudioTrack) {
        audioManagerService.addAudio(track)?.also {
            selectTrack(trackService.addTrack(track.apply { mediaPlayer = it }))
            //  _showLayer.update { true }
        }

    }

    fun selectTrack(track: AudioTrack) {
        _selectedTrack.update { track.id }
        _selectedTrackTime.update { track.mediaPlayer?.mediaPlayer?.duration }
    }

    fun pause() {
        stopAll()
    }


}
