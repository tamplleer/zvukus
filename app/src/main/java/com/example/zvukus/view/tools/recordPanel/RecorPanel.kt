package com.example.zvukus.view.tools.recordPanel

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.zvukus.view.tools.recordPanel.components.PlayButton
import com.example.zvukus.view.tools.recordPanel.components.RecordMicButton
import com.example.zvukus.view.tools.recordPanel.components.RecordTrackButton

@Composable
fun RecordPanel() {
    Row {
        RecordMicButton()
        RecordTrackButton()
        PlayButton()
    }
}

fun getColor(disable: Boolean, isRecording: Boolean): Color {
    if (disable) {
        return Color.Gray
    }
    return if (isRecording) {
        Color.Red
    } else Color.Black
}



