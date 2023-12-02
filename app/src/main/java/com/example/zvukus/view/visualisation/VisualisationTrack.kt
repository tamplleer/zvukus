package com.example.zvukus.view.visualisation

import android.util.Log
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.R
import com.example.zvukus.screen.visual.VisualViewModel
import kotlin.random.Random


@Composable
fun VisualisationTrack(modifier: Modifier, visualViewModel: VisualViewModel = hiltViewModel()) {

    var maxOffsetX by remember { mutableFloatStateOf(0f) }
    var minOffsetX by remember { mutableFloatStateOf(0f) }

    var maxOffsetY by remember { mutableFloatStateOf(0f) }
    var minOffsetY by remember { mutableFloatStateOf(0f) }

    val isRunningTrack by visualViewModel.isRunningTrack.collectAsState()

    val listObject: List<VisualObject> =
        listOf<VisualObject>(
            Object(R.drawable.fig1, "1", Pair(500, 400), 100f, Color.Green, 5000),
            Object(
                R.drawable.fig3,
                "2",
                Pair(100, 200),
                200f,
                MaterialTheme.colorScheme.primary,
                2000
            ),
            Object(R.drawable.fig4, "3", Pair(50, 400), 100f, Color.Green, 2000),
            Object(
                R.drawable.fig5,
                "4",
                Pair(500, 300),
                300f,
                MaterialTheme.colorScheme.secondary,
                3000
            ),
            Object(
                R.drawable.fig7,
                "5",
                Pair(maxOffsetX.toInt(), 100),
                70f,
                MaterialTheme.colorScheme.primary,
                3000
            ),
            Object(
                R.drawable.fig8,
                "6",
                Pair(0, 900),
                150f,
                MaterialTheme.colorScheme.primary,
                4000
            ),
            Object(R.drawable.fig9, "7", Pair(maxOffsetX.toInt(), 900), 10f, Color.Green, 1),
            Object(
                R.drawable.fig10,
                "8",
                Pair(300, 900),
                200f,
                MaterialTheme.colorScheme.primary,
                2000
            ),
            Object(
                R.drawable.zavituly,
                "0",
                Pair(100, 100),
                50f,
                MaterialTheme.colorScheme.primary,
                2000
            ),


            Object(
                R.drawable.fig7,
                "5",
                Pair(maxOffsetX.toInt(), 700),
                70f,
                MaterialTheme.colorScheme.primary,
                3000
            ),
            Object(
                R.drawable.fig8,
                "6",
                Pair(200, 100),
                55f,
                Color.Green,
                1100
            ),
            Object(
                R.drawable.fig9,
                "7",
                Pair(maxOffsetX.toInt(), 100),
                109f,
                Color.Green,
                1000
            ),
            Object(
                R.drawable.fig10,
                "8",
                Pair(900, 900),
                50f,
                Color.Green,
                5000
            ),
        )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .onGloballyPositioned { position ->
                val positionX = position.size.width.toFloat()
                val positionY = position.size.height.toFloat()
                maxOffsetX = positionX
                maxOffsetY = positionY
            }
    ) {
        listObject.map {
            VisualObject(
                it,
                minOffsetX, maxOffsetX, maxOffsetY, minOffsetY, isRunningTrack
            )
        }


    }
}


@Composable
fun VisualObject(
    visualObject: VisualObject, minOffsetX: Float,
    maxOffsetX: Float,
    maxOffsetY: Float,
    minOffsetY: Float,
    play: Boolean
) {
    var currentOffsetX by remember { mutableFloatStateOf(visualObject.defaultPosition.first.toFloat()) }
    var currentOffsetY by remember { mutableFloatStateOf(visualObject.defaultPosition.first.toFloat()) }
    var sizePoint by remember { mutableFloatStateOf(0f) }
    var isDrag by remember { mutableStateOf(false) }

    var targetPositionX by remember { mutableFloatStateOf(10f) }
    var finishTargetX by remember { mutableStateOf(true) }
    var finishTargetY by remember { mutableStateOf(true) }

    var targetPositionY by remember { mutableFloatStateOf(100f) }


    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            tween(visualObject.animationTime),
            RepeatMode.Reverse
        ),
        label = "scale"
    )

    val rotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(visualObject.animationTime),
            RepeatMode.Reverse
        ),
        label = "rotate"
    )

    val transition = updateTransition(
        targetState = finishTargetX,
        label = "playing"
    )

    val transitionY = updateTransition(
        targetState = finishTargetY,
        label = "playing"
    )

    val animateMove: Float by transition.animateFloat(
        transitionSpec = {
            tween(
                visualObject.animationTime,
                easing = CubicBezierEasing(0.3f, 0.4f, 0.5f, 0.1f)
            )
        },
        label = "dragSize"
    ) { state ->
        when (state) {
            false -> targetPositionX
            true -> {
                targetPositionX
            }
        }
    }
    val animateMoveY: Float by transitionY.animateFloat(
        transitionSpec = {
            tween(
                visualObject.animationTime,
                easing = CubicBezierEasing(0.3f, 0.4f, 0.5f, 0.1f)
            )
        },
        label = "dragSize"
    ) { state ->
        when (state) {
            false -> targetPositionY
            true -> {
                targetPositionY
            }
        }
    }

    LaunchedEffect(animateMove, finishTargetX) {
        if (animateMove == targetPositionX) {
            val random = Random.Default

            targetPositionX = random.nextFloat() * (maxOffsetX - minOffsetX) + minOffsetX
            finishTargetX = !finishTargetX
        }
    }
    LaunchedEffect(animateMoveY, finishTargetY) {
        if (animateMoveY == targetPositionY) {
            val random = Random.Default

            targetPositionY = random.nextFloat() * (maxOffsetX - minOffsetX) + minOffsetX
            finishTargetY = !finishTargetY
        }
    }

    LaunchedEffect(isDrag) {
        if (isDrag) {
            currentOffsetX = animateMove
            currentOffsetY = animateMoveY
        } else {
            targetPositionX = currentOffsetX
            targetPositionY = currentOffsetY
        }
    }


    Box(modifier = Modifier
        .size(visualObject.size.dp)
        .graphicsLayer(
            translationX = if (isDrag || !play) currentOffsetX else {
                animateMove
            },
            translationY = if (isDrag || !play) currentOffsetY else {
                animateMoveY
            },
            scaleX = if (play) scale else 1f,
            scaleY = if (play) scale else 1f,

            )
        .pointerInput(maxOffsetX, minOffsetY, maxOffsetY, maxOffsetX) {
            detectDragGestures(
                onDragStart = {
                    Log.i("aa", "start")
                },
                onDrag = { _, dragAmount ->
                    val nextX = currentOffsetX + dragAmount.x
                    isDrag = true
                    if (nextX + sizePoint < maxOffsetX && nextX > minOffsetX) {
                        currentOffsetX = nextX
                    }
                    val nextY = currentOffsetY + dragAmount.y
                    if (nextY + sizePoint < maxOffsetY && nextY > minOffsetY) {
                        currentOffsetY = nextY
                    }
                },
                onDragEnd = {
                    isDrag = false
                },
            )

        }
        .onGloballyPositioned { position ->
            sizePoint = position.size.width.toFloat()

        }) {
        Icon(
            modifier = Modifier.rotate(if (play) rotate else 0f),
            tint = visualObject.color,
            painter = painterResource(
                visualObject.icon
            ),
            contentDescription = "visual object"
        )
    }
}


interface VisualObject {
    val icon: Int
    val id: String
    val defaultPosition: Pair<Int, Int>
    val size: Float
    val color: Color
    val animationTime: Int
}

data class Object(
    override val icon: Int, override val id: String,
    override val defaultPosition: Pair<Int, Int>,
    override val size: Float, override val color: Color, override val animationTime: Int
) : VisualObject {

}
