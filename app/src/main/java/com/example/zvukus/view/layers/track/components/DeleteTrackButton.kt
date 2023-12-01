package com.example.zvukus.view.layers.track.components

import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.PlayerViewModel
import com.example.zvukus.R
import com.example.zvukus.model.AudioTrack

@Composable
fun DeleteTrackButton(track: AudioTrack, playerViewModel: PlayerViewModel = hiltViewModel()) {
    DeleteTrackButtonUi(playerViewModel::removeTrack, track)
}

@Composable
fun DeleteTrackButtonUi(remove: (AudioTrack) -> Unit, track: AudioTrack) {
    Button(onClick = { remove(track) }) {
        Icon(
            painter = painterResource(R.drawable.delete),
            contentDescription = "start recording"
        )
    }
}