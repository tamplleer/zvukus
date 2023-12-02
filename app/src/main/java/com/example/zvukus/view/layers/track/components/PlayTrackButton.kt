package com.example.zvukus.view.layers.track.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.R
import com.example.zvukus.model.AudioTrack
import com.example.zvukus.screen.main.PlayerViewModel

@Composable
fun PlayTrackButton(track: AudioTrack,  toVisualTrack: () -> Unit, playerViewModel: PlayerViewModel = hiltViewModel()) {
    val selectedTrackPlaying by playerViewModel.selectedTrackPlaying.collectAsState()
    val playTrack = playerViewModel::playTrack
    val stopTrack = playerViewModel::stopTrack

    fun play() {
        if (selectedTrackPlaying == track.id) {
            stopTrack(track.id, track.mediaPlayer)
        } else {
           // playTrack(track)
            toVisualTrack()
        }
    }
    PlayTrackButtonUi(::play, selectedTrackPlaying, track)
}

@Composable
fun PlayTrackButtonUi(
    play: () -> Unit,
    selectedTrackPlaying: String,
    track: AudioTrack
) {
    IconButton(onClick = {
        play()
    }) {
        Icon(
            painter = if (selectedTrackPlaying == track.id) painterResource(R.drawable.stop) else painterResource(
                R.drawable.play
            ),
            contentDescription = "play track"
        )
    }
}