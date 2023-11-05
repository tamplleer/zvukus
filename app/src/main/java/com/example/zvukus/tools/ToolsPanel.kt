package com.example.zvukus.tools

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.PlayerViewModel

@Composable
fun ToolsPanel(
    modifier: Modifier,
    context: Context,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {

    val listTrack = playerViewModel.listTrack.collectAsState()
    val showCloseLayer = playerViewModel::showCloseLayer

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Button(onClick = { showCloseLayer() }) {
            Text(text = "Layer")
        }

        RecordPanel()
    }

}