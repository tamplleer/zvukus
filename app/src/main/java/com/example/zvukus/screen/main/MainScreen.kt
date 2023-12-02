package com.example.zvukus.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.zvukus.view.layers.Layers
import com.example.zvukus.view.samples.Samples
import com.example.zvukus.view.timeline.Timeline
import com.example.zvukus.view.tools.ToolsPanel
import com.example.zvukus.view.workspace.WorkSpace

@Composable
fun mainRouter(
    onBackClick: () -> Unit,
    toVisualTrack: () -> Unit,
) {
    mainScreen(toVisualTrack)
}

@Composable
fun mainScreen(toVisualTrack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Samples(Modifier.zIndex(1f))
        Column(modifier = Modifier) {
            Spacer(modifier = Modifier.weight(0.2f))
            Box(modifier = Modifier.weight(0.74f)) {
                Column {
                    WorkSpace(Modifier.weight(0.9f))
                    Timeline(Modifier.weight(0.1f))
                }
                Layers(Modifier.align(Alignment.BottomStart),toVisualTrack)
            }
            ToolsPanel(Modifier.weight(0.06f),toVisualTrack)
        }
    }
}