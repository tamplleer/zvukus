package com.example.zvukus.view.workspace

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.model.AudioTrack
import com.example.zvukus.screen.main.PlayerViewModel
import java.lang.StrictMath.round

fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

@Composable
fun WorkSpace(modifier: Modifier, playerViewModel: PlayerViewModel = hiltViewModel()) {
    val trackId by playerViewModel.selectedTrackId.collectAsState()
    WorkSpaceUi(
        modifier,
        trackId,
        playerViewModel::changeIntervalTime,
        playerViewModel::getTrackById,
        playerViewModel::changeVolume
    )

}

@Composable
fun WorkSpaceUi(
    modifier: Modifier,
    trackId: String?,
    changeIntervalTime: (Float) -> Unit,
    getTrackById: (String?) -> AudioTrack?,
    changeVolume: (Float) -> Unit
) {

    var currentOffsetX by remember { mutableFloatStateOf(50f) }
    var currentOffsetY by remember { mutableFloatStateOf(50f) }

    var maxOffsetX by remember { mutableFloatStateOf(0f) }
    var minOffsetX by remember { mutableFloatStateOf(0f) }

    var maxOffsetY by remember { mutableFloatStateOf(0f) }
    var minOffsetY by remember { mutableFloatStateOf(0f) }

    fun setCurrentOffsetX(value: Float) {
        currentOffsetX = value
    }

    fun setCurrentOffsetY(value: Float) {
        currentOffsetY = value
    }

    fun setMinMaxYX(xPosition: Float, yPosition: Float) {
        maxOffsetX = xPosition
        minOffsetX = 0f

        maxOffsetY = yPosition
        minOffsetY = 0f
    }


    LaunchedEffect(trackId) {
        getTrackById(trackId)?.let {
            currentOffsetX =  (maxOffsetX / 10) * (it.intervalTime ?: 0f)
            currentOffsetY = 10 * (maxOffsetY / 10) * it.volume
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)

    ) {
        Row {
            VerticalIndicator(currentOffsetY, maxOffsetY)

            IndicatorField(
                minOffsetX,
                maxOffsetX,
                maxOffsetY,
                minOffsetY,
                currentOffsetX,
                currentOffsetY,
                changeIntervalTime,
                changeVolume,
                ::setCurrentOffsetX,
                ::setCurrentOffsetY,
                ::setMinMaxYX
            )

        }
        HorizontalIndicator(currentOffsetX, maxOffsetX)
    }
}


@Composable
fun HorizontalIndicator(currentOffsetX: Float, maxOffsetX: Float) {
    var width by remember { mutableFloatStateOf(0f) }

    fun setPosition(): Float {
        if (currentOffsetX - width / 2 < 0) {
            return width / 2
        } else if (maxOffsetX > currentOffsetX + width / 2) {
            return currentOffsetX
        }
        return maxOffsetX - (width / 2)
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
                    translationX = setPosition(),
                    translationY = 0f,
                )
                .padding(2.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .width(60.dp)
                .background(MaterialTheme.colorScheme.secondary)
                .onGloballyPositioned { position ->
                    width = position.size.width.toFloat()

                }

        ) {
            Text(
                modifier = Modifier.align(
                    Alignment.Center
                ),
                text = "Speed ${round(currentOffsetX / (maxOffsetX / 10))}",
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun VerticalIndicator(currentOffsetY: Float, maxOffsetY: Float) {
    var height by remember { mutableFloatStateOf(0f) }
    Column(
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .width(20.dp)
            .background(MaterialTheme.colorScheme.background)

    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .graphicsLayer(
                    translationX = 0f,
                    translationY = if (maxOffsetY > currentOffsetY + height+5) currentOffsetY else (maxOffsetY - height-5),
                )
                .clip(RoundedCornerShape(10.dp))
                .height(70.dp)
                .width(20.dp)
                .background(MaterialTheme.colorScheme.secondary)
                .onGloballyPositioned { position ->
                    height = position.size.height.toFloat()

                },


            ) {
            Text(
                modifier = Modifier,
                color = MaterialTheme.colorScheme.background,
                text = " V${round(currentOffsetY / (maxOffsetY / 10))}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun IndicatorField(
    minOffsetX: Float,
    maxOffsetX: Float,
    maxOffsetY: Float,
    minOffsetY: Float,
    globalOffsetX: Float,
    globalOffsetY: Float,
    changeIntervalTime: (Float) -> Unit,
    changeVolume: (Float) -> Unit,
    setCurrentOffsetX: (Float) -> Unit,
    setCurrentOffsetY: (Float) -> Unit,
    setMinMaxYX: (Float, Float) -> Unit,
) {
    var sizePoint by remember { mutableFloatStateOf(0f) }
    var currentOffsetX by remember { mutableFloatStateOf(50f) }
    var currentOffsetY by remember { mutableFloatStateOf(50f) }
    var isDrag by remember { mutableStateOf(false) }

    LaunchedEffect(globalOffsetX, globalOffsetY) {
        currentOffsetX = globalOffsetX
        currentOffsetY = globalOffsetY
    }

    val transition = updateTransition(
        targetState = isDrag,
        label = "playing"
    )

    val animateDrag: Float by transition.animateFloat(
        transitionSpec = {
            tween(100, easing = CubicBezierEasing(0.3f, 0.4f, 0.5f, 0.1f))
        },
        label = "dragSize"
    ) { state ->
        when (state) {
            false -> 15f
            true -> 20f
        }

    }


    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .background(MaterialTheme.colorScheme.secondary)
            .onGloballyPositioned { position ->
                val positionX = position.size.width.toFloat()
                val positionY = position.size.height.toFloat()

                setMinMaxYX(positionX, positionY)
            }
    ) {
        Box(modifier = Modifier
            .size(animateDrag.dp)
            .graphicsLayer(
                translationX = currentOffsetX,
                translationY = currentOffsetY,
            )
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Green)
            .pointerInput(maxOffsetX, minOffsetY, maxOffsetY, maxOffsetX) {
                detectDragGestures(
                    onDragStart = {

                    },
                    onDrag = { _, dragAmount ->
                        val nextX = currentOffsetX + dragAmount.x
                        isDrag = true
                        if (nextX + sizePoint < maxOffsetX && nextX > minOffsetX) {
                            setCurrentOffsetX(nextX)
                        }
                        val nextY = currentOffsetY + dragAmount.y
                        if (nextY + sizePoint < maxOffsetY && nextY > minOffsetY) {
                            setCurrentOffsetY(nextY)
                        }
                    },
                    onDragEnd = {
                        isDrag = false
                        changeVolume(round(currentOffsetY / (maxOffsetY / 10)).toFloat() / 10)
                        changeIntervalTime(round(currentOffsetX / (maxOffsetX / 100) / 10).toFloat())
                    },
                )

            }
            .onGloballyPositioned { position ->
                sizePoint = position.size.width.toFloat()

            }) {}
    }
}
