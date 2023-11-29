package com.example.zvukus.view.timeline

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.PlayerViewModel


@Composable
fun Timeline(modifier: Modifier, playerViewModel: PlayerViewModel = hiltViewModel()) {
    val track by playerViewModel.selectedTrackTime.collectAsState()
    val isPlay by playerViewModel.selectedTrackPlay.collectAsState()

    Timeline(modifier = modifier, track = track, isPlay = isPlay)

}

@Composable
fun Timeline(modifier: Modifier, track: Int?, isPlay: Boolean) {

    val localDensity = LocalDensity.current

    var maxOffsetX by remember { mutableFloatStateOf(0f) }

    val transition = updateTransition(
        targetState = isPlay,
        label = "playing"
    )


    val animatePlay: Float by transition.animateFloat(
        transitionSpec = {
            tween(track ?: 2000, easing = CubicBezierEasing(0.0f, 0.0f, 0.0f, 0.0f))
        },
        label = "playing"
    ) { state ->
        when (state) {
            false -> 10f
            true -> maxOffsetX
        }

    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primary)
            .onGloballyPositioned { position ->
                maxOffsetX = position.size.width.toFloat()
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp))
                .width(with(localDensity) { animatePlay.toDp() })
                .background(Color.Green)
        ) {

        }
    }
}