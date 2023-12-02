package com.example.zvukus.screen.visual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.R
import com.example.zvukus.view.timeline.TimelineVisual
import com.example.zvukus.view.visualisation.VisualisationTrack

@Composable
fun visualTrackRouter(
    onBackClick: () -> Unit,
) {
    VisualTrackScreen(onBackClick)
}

@Composable
fun VisualTrackScreen(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Column(modifier = Modifier) {
            VisualHeader(Modifier.weight(0.1f), onBackClick)
            Column(
                modifier = Modifier
                    .weight(0.9f)

            ) {
                VisualisationTrack(modifier = Modifier.weight(0.9f))
                Column(modifier = Modifier.weight(0.1f)) {
                    TimelineVisual(modifier = Modifier.fillMaxHeight(0.5f))
                    TrackControl()
                }
            }
        }
    }
}

@Composable
fun VisualHeader(
    modifier: Modifier,
    onBackClick: () -> Unit,
    visualViewModel: VisualViewModel = hiltViewModel()
) {
    val track by visualViewModel.trackGlobal.collectAsState()
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            onBackClick()
        }) {
            Icon(
                painter = painterResource(
                    R.drawable.back
                ),
                contentDescription = "back"
            )
        }
        Text(text = track?.name ?: "Current track")
    }
}

@Composable
fun TrackControl(visualViewModel: VisualViewModel = hiltViewModel()) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        PlayTrackButtonVisual()
    }

}


@Composable
fun PlayTrackButtonVisual(
    visualViewModel: VisualViewModel = hiltViewModel()
) {
    val selectedTrackPlaying by visualViewModel.trackGlobal.collectAsState()
    val isRunningTrack by visualViewModel.isRunningTrack.collectAsState()

    val playTrack = visualViewModel::playGlobalTrack
    val stopTrack = visualViewModel::stopTrackGlobal

    fun play() {
        if (isRunningTrack) {
            stopTrack()
        } else {
            playTrack()
        }
    }
    PlayTrackButtonVisualUi(::play, selectedTrackPlaying?.id ?: "", isRunningTrack)
}

@Composable
fun PlayTrackButtonVisualUi(
    play: () -> Unit,
    selectedTrackPlaying: String,
    isRunningTrack: Boolean
) {
    IconButton(onClick = {
        play()
    }) {
        Icon(
            painter = if (isRunningTrack) painterResource(R.drawable.pause) else painterResource(
                R.drawable.play
            ),
            contentDescription = "play track"
        )
    }
}