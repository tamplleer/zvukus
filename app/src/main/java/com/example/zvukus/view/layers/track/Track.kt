package com.example.zvukus.view.layers.track

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.screen.main.PlayerViewModel
import com.example.zvukus.model.AudioTrack
import com.example.zvukus.view.layers.track.components.DeleteTrackButton
import com.example.zvukus.view.layers.track.components.MuteTrackButton
import com.example.zvukus.view.layers.track.components.PlayTrackButton

@Composable
fun Track(
    track: AudioTrack,
    uiUpdate: Boolean,
    changeUiUpdate: () -> Unit,
    toVisualTrack: () -> Unit,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val select = playerViewModel::selectTrack
    val selected by playerViewModel.selectedTrackId.collectAsState()
    uiUpdate

    TrackUi(select, selected, changeUiUpdate, track,toVisualTrack)

}

@Composable
fun TrackUi(
    select: (AudioTrack) -> Unit,
    selected: String?,
    changeUiUpdate: () -> Unit,
    track: AudioTrack,
    toVisualTrack: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(end = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(0.8f)
                .background(if (selected == track.id) Color.Green else MaterialTheme.colorScheme.primary)
                .clickable { select(track) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Text(text = track.name, modifier = Modifier.padding(start = 10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PlayTrackButton(track,toVisualTrack)
                MuteTrackButton(track, changeUiUpdate)

            }
        }
        DeleteTrackButton(track)
    }
}