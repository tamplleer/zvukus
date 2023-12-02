package com.example.zvukus.view.tools.recordPanel.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.screen.main.PlayerViewModel
import com.example.zvukus.R
import com.example.zvukus.view.tools.recordPanel.getColor
import java.util.UUID

@Composable
fun RecordTrackButton(playerViewModel: PlayerViewModel = hiltViewModel()) {
    val selectedRecord by playerViewModel.selectedRecord.collectAsState()
    var recordTrackId by remember {
        mutableStateOf<String?>(null)
    }

    val shared = playerViewModel::shared

    fun recordTrack() {
        if (recordTrackId == selectedRecord) {
            recordTrackId = null
            playerViewModel.stopAll()
            playerViewModel.stopRecordTrack()
            playerViewModel.setRecordId("")
            shared()
        } else {
            val id = UUID.randomUUID().toString()
            playerViewModel.setRecordId(id)
            recordTrackId = id
            playerViewModel.playAll()
            playerViewModel.recordTrack()

        }
    }

    RecordTrackButtonUi(
        recordTrack = ::recordTrack,
        isRecording = selectedRecord == recordTrackId,
        enabled = selectedRecord.isBlank() || selectedRecord == recordTrackId
    )
}

@Composable
fun RecordTrackButtonUi(recordTrack: () -> Unit, isRecording: Boolean, enabled: Boolean) {
    IconButton(enabled = enabled, onClick = { recordTrack() }) {
        Icon(
            painter = painterResource(R.drawable.record),
            tint = getColor(!enabled, isRecording),
            contentDescription = "start recording"
        )
    }
}