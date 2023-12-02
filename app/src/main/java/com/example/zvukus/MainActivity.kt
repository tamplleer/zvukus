package com.example.zvukus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.zvukus.navigate.AppNavHost
import com.example.zvukus.screen.main.PlayerViewModel
import com.example.zvukus.screen.visual.VisualViewModel
import com.example.zvukus.ui.theme.ZvukusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val playerViewModel: PlayerViewModel by viewModels()
    private val visualViewModel: VisualViewModel by viewModels()
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
                    AppNavHost()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
        visualViewModel.pause()
    }
}


