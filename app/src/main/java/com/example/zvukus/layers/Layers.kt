package com.example.zvukus.layers

import android.content.res.Resources.Theme
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.PlayerViewModel
import com.example.zvukus.R
import com.example.zvukus.services.AudioTrack

@Composable
fun Layers(modifier: Modifier, playerViewModel: PlayerViewModel = hiltViewModel()) {
    val listTrack by playerViewModel.listTrack.collectAsState()
    val listTrackSize by playerViewModel.listTrackSize.collectAsState()
    val showLayer by playerViewModel.showLayer.collectAsState()
    LayersUi(modifier, listTrack.values.toList(), listTrackSize, showLayer)
}

@Composable
fun LayersUi(modifier: Modifier, listTrack: List<AudioTrack>, size: Int, showLayer: Boolean) {
    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current
    Column(modifier = modifier.clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.outline).padding(2.dp).clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.background)) {
        AnimatedVisibility(
            visible = showLayer,
            enter = slideInVertically {
                // Slide in from 40 dp from the top.
                with(density) { 40.dp.roundToPx() }
            } + expandVertically(
                expandFrom = Alignment.Bottom
            ) + fadeIn(
                initialAlpha = 0.3f
            ),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            if (size != 0) {
                LazyColumn(
                    state = lazyListState, modifier = Modifier
                        .fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        count = size,
                    ) {
                        Track(listTrack[it])
                    }
                }
            }
        }
    }
}

@Composable
fun Track(track: AudioTrack, playerViewModel: PlayerViewModel = hiltViewModel()) {
    val playTrack = playerViewModel::playTrack
    val stopTrack = playerViewModel::stopTrack
    val remove = playerViewModel::removeTrack
    val mute = playerViewModel::mute
    val select = playerViewModel::selectTrack
    val isSelected by playerViewModel.selectedTrackId.collectAsState()
    val selectedTrackPlaying by playerViewModel.selectedTrackPlaying.collectAsState()
    val uiUpdate by playerViewModel.uiUpdate.collectAsState()


    Log.i("aa", "$uiUpdate")

    fun play() {
        if (selectedTrackPlaying == track.id) {
            stopTrack(track.id, track.mediaPlayer)
        } else {
            playTrack(track)
        }
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {

        Row(

            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(0.8f)
                .background(if (isSelected == track.id) Color.Green else MaterialTheme.colorScheme.primary)
                .clickable { select(track) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Text(text = track.name, modifier = Modifier.padding(start = 10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
                IconButton(onClick = { mute(track, !track.mute) }) {
                    Icon(
                        painter = if (track.mute) painterResource(R.drawable.volume_off) else painterResource(
                            R.drawable.volume
                        ),
                        contentDescription = "mute"
                    )
                }
            }


        }
        Button(onClick = { remove(track) }) {
            Icon(
                painter = painterResource(R.drawable.delete),
                contentDescription = "start recording"
            )
        }
    }
}