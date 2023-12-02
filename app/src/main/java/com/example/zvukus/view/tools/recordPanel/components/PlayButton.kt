package com.example.zvukus.view.tools.recordPanel.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.screen.main.ALL_TRACK_SELECTED
import com.example.zvukus.screen.main.PlayerViewModel
import com.example.zvukus.R

@Composable
fun PlayButton(toVisualTrack: () -> Unit, playerViewModel: PlayerViewModel = hiltViewModel()) {
    val selectedTrackPlaying by playerViewModel.selectedTrackPlaying.collectAsState()

    fun play() = if (selectedTrackPlaying == ALL_TRACK_SELECTED) {
        playerViewModel.stopAll()
    } else {
        playerViewModel.playAll()
        toVisualTrack()

    }

    PlayButtonUi(play = ::play, selectedTrackPlaying = selectedTrackPlaying)
}

@Composable
fun PlayButtonUi(play: () -> Unit, selectedTrackPlaying: String) {
    IconButton(onClick = { play() }) {
        Icon(
            painter = if (selectedTrackPlaying == ALL_TRACK_SELECTED) painterResource(R.drawable.stop) else painterResource(
                R.drawable.play
            ),
            contentDescription = "Play record"
        )
    }
}