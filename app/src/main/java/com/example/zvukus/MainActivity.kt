package com.example.zvukus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import com.example.zvukus.ui.theme.ZvukusTheme
import com.example.zvukus.view.layers.Layers
import com.example.zvukus.view.samples.Samples
import com.example.zvukus.view.timeline.Timeline
import com.example.zvukus.view.tools.ToolsPanel
import com.example.zvukus.view.workspace.WorkSpace
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val playerViewModel: PlayerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            0
        )

        setContent {

            ZvukusTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
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
                                Layers(Modifier.align(Alignment.BottomStart))
                            }
                            ToolsPanel(Modifier.weight(0.06f))
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
    }
}


