package com.example.zvukus.workspace

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.PlayerViewModel
import java.lang.StrictMath.round


@Composable
fun WorkSpace(modifier: Modifier, playerViewModel: PlayerViewModel = hiltViewModel()) {
    val changeVolume = playerViewModel::changeVolume
    val getTrackById = playerViewModel::getTrackById
    val changeIntervalTime = playerViewModel::changeIntervalTime
    val trackId by playerViewModel.selectedTrackId.collectAsState()

    var currentOffsetX by remember { mutableStateOf(50f) }
    var currentOffsetY by remember { mutableStateOf(50f) }

    var maxOffsetX by remember { mutableStateOf(0f) }
    var minOffsetX by remember { mutableStateOf(0f) }

    var maxOffsetY by remember { mutableStateOf(0f) }
    var minOffsetY by remember { mutableStateOf(0f) }

    LaunchedEffect(trackId) {
        getTrackById(trackId)?.let {
            currentOffsetX = 10 * (maxOffsetY / 50) * (it.intervalTime ?: 0f)
            currentOffsetY = 10 * (maxOffsetX / 10) * it.volume
        }
    }


    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .width(20.dp)
                    .background(MaterialTheme.colorScheme.background)

            ) {
                Box(
                    modifier = Modifier
                        .graphicsLayer(
                            translationX = 0f,
                            translationY = currentOffsetY,
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .height(70.dp)
                        .width(20.dp)
                        .background(MaterialTheme.colorScheme.secondary)

                ) {
                    Text(
                        modifier = Modifier.rotate(-90f),
                        text = "Volume ${round(currentOffsetY / (maxOffsetY / 10))}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .background(Color.Gray)
                    .onGloballyPositioned { position ->
                        val positionX = position.size.width.toFloat()
                        val positionY = position.size.height.toFloat()

                        maxOffsetX = positionX
                        minOffsetX = 0f

                        maxOffsetY = positionY
                        minOffsetY = 0f
                    }
            ) {
                Box(modifier = Modifier
                    .size(10.dp)
                    .graphicsLayer(
                        translationX = currentOffsetX,
                        translationY = currentOffsetY,
                    )
                    .background(Color.Green)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {

                            },
                            onDrag = { _, dragAmount ->

                                val nextX = currentOffsetX + dragAmount.x
                                if (nextX < maxOffsetX && nextX > minOffsetX) {
                                    currentOffsetX = nextX
                                } else {
                                    val x = 1
                                }
                                val nextY = currentOffsetY + dragAmount.y
                                if (nextY < maxOffsetY && nextY > minOffsetY) {
                                    currentOffsetY = nextY
                                } else {
                                    val x = 1
                                }
                            },
                            onDragEnd = {
                                changeVolume(round(currentOffsetY / (maxOffsetY / 10)).toFloat() / 10)
                                changeIntervalTime(round(currentOffsetX / (maxOffsetX / 50)).toFloat() / 10)
                            },
                        )

                    }) {

                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer(
                        translationX = currentOffsetX,
                        translationY = 0f,
                    )
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .width(60.dp)
                    .background(MaterialTheme.colorScheme.secondary)

            ) {
                Text(
                    text = "Speed ${round(currentOffsetX / (maxOffsetX / 10))} +",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}