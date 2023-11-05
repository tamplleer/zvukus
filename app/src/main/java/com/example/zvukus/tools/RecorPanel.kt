package com.example.zvukus.tools

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.PlayerViewModel
import com.example.zvukus.R

@Composable
fun RecordPanel(playerViewModel: PlayerViewModel = hiltViewModel()) {
    val shared = playerViewModel::shared

    var isRecording by remember {
        mutableStateOf(false)
    }
    val selectedTrackPlaying by playerViewModel.selectedTrackPlaying.collectAsState()

    var isRecordTrack by remember {
        mutableStateOf(false)
    }


    fun recordMic() = if (isRecording) {
        isRecording = false
        playerViewModel.stopRecord()
    } else {
        isRecording = true
        playerViewModel.recordMic()

    }

    fun play() = if (selectedTrackPlaying == "all") {
        Log.i("aa", "stop")
        playerViewModel.stopAll()
    } else {
        Log.i("aa", "start")
        playerViewModel.playAll()

    }

    fun recordTrack() {
        isRecording = false

        if (isRecordTrack) {
            isRecordTrack = false
            playerViewModel.stopAll()
            playerViewModel.stopRecord()
            shared()
        } else {
            isRecordTrack = true
            playerViewModel.playAll()
            playerViewModel.recordTrack()

        }
    }
    Row {
        IconButton(onClick = { recordMic() }) {
            Icon(
                painter = painterResource(R.drawable.mic),
                tint = if (isRecording) Color.Red else Color.Black,
                contentDescription = "Record mic"
            )
        }
        IconButton(onClick = { recordTrack() }) {
            Icon(
                painter = painterResource(R.drawable.record),
                tint = if (isRecordTrack) Color.Red else Color.Black,
                contentDescription = "start recording"
            )
        }
        IconButton(onClick = { play() }) {
            Icon(
                painter = if (selectedTrackPlaying == "all") painterResource(R.drawable.stop) else painterResource(
                    R.drawable.play
                ),
                contentDescription = "Play record"//todo fix name
            )
        }
    }
}