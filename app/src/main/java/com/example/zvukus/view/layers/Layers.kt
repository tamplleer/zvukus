package com.example.zvukus.view.layers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.screen.main.PlayerViewModel
import com.example.zvukus.model.AudioTrack
import com.example.zvukus.view.layers.track.Track

@Composable
fun Layers(modifier: Modifier, toVisualTrack: () -> Unit,playerViewModel: PlayerViewModel = hiltViewModel()) {
    val listTrack by playerViewModel.listTrack.collectAsState()
    val showLayer by playerViewModel.showLayer.collectAsState()
    LayersUi(modifier, listTrack, listTrack.size, showLayer,toVisualTrack)
}

@Composable
fun LayersUi(modifier: Modifier, listTrack: List<AudioTrack>, size: Int, showLayer: Boolean,toVisualTrack: () -> Unit) {
    val lazyListState = rememberLazyListState()
    var uiUpdate by remember {
        mutableStateOf(false)
    }

    fun changeUiUpdate() {
        uiUpdate = !uiUpdate
    }

    val density = LocalDensity.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.outline)
            .padding(2.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = showLayer,
            enter = slideInVertically {
                with(density) { 40.dp.roundToPx() }
            } + expandVertically(
                expandFrom = Alignment.Bottom
            ) + fadeIn(
                initialAlpha = 0.3f
            ),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {

            LazyColumn(
                state = lazyListState, modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (size != 0) {
                    items(
                        count = size,
                    ) {
                        Track(listTrack[it], uiUpdate, ::changeUiUpdate,toVisualTrack)
                    }
                } else {
                    item {
                        Text(text = "You haven't had time to add a layer yet")
                    }
                }
            }
        }
    }
}