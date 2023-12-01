package com.example.zvukus.view.layers.track.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.PlayerViewModel
import com.example.zvukus.R
import com.example.zvukus.model.AudioTrack

@Composable
fun MuteTrackButton(
    track: AudioTrack,
    changeUiUpdate: () -> Unit,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    MuteTrackButtonUi(changeUiUpdate, playerViewModel::mute, track)
}

@Composable
fun MuteTrackButtonUi(
    changeUiUpdate: () -> Unit,
    mute: (AudioTrack, Boolean) -> Unit,
    track: AudioTrack
) {
    IconButton(onClick = {
        mute(track, !track.mute)
        changeUiUpdate()
    }) {
        Icon(
            painter = if (track.mute) painterResource(R.drawable.volume_off) else painterResource(
                R.drawable.volume
            ),
            contentDescription = "mute"
        )
    }
}
