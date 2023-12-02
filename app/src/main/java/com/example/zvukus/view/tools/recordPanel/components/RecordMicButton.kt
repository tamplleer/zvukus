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
fun RecordMicButton(playerViewModel: PlayerViewModel = hiltViewModel()) {
    val selectedRecord by playerViewModel.selectedRecord.collectAsState()
    var recordId by remember {
        mutableStateOf<String?>(null)
    }

    fun recordMic() = if (selectedRecord == recordId) {
        recordId = null
        playerViewModel.stopRecord()
        playerViewModel.setRecordId("")
    } else {
        val id = UUID.randomUUID().toString()
        playerViewModel.setRecordId(id)
        recordId = id
        playerViewModel.recordMic()

    }

    RecordMicButtonUi(
        recordMic = ::recordMic,
        isRecording = selectedRecord == recordId,
        enabled = selectedRecord.isBlank() || selectedRecord == recordId
    )
}

@Composable
fun RecordMicButtonUi(recordMic: () -> Unit, isRecording: Boolean, enabled: Boolean) {
    IconButton(enabled = enabled, onClick = { recordMic() }) {
        Icon(
            painter = painterResource(R.drawable.mic),
            tint = getColor(!enabled, isRecording),
            contentDescription = "Record mic"
        )
    }
}