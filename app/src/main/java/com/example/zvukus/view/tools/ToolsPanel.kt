package com.example.zvukus.view.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.PlayerViewModel
import com.example.zvukus.view.tools.recordPanel.RecordPanel

@Composable
fun ToolsPanel(
    modifier: Modifier,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val showCloseLayer = playerViewModel::showCloseLayer
    ToolsPanelUi(modifier, showCloseLayer)

}

@Composable
fun ToolsPanelUi(modifier: Modifier, showCloseLayer: () -> Unit) {
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