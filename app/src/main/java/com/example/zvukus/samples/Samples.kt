package com.example.zvukus.samples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import com.example.zvukus.services.AudioTrack
import com.example.zvukus.R
import java.util.UUID


@Composable
fun Samples(modifier: Modifier) {
    var expanded by remember { mutableStateOf("") }
    fun updateExpanded(id: String) {
        expanded = id
    }

    val kicks = listOf(
        AudioTrack(
            UUID.randomUUID().toString(),
            1.0f,
            1,
            1,
            4f,
            "Барабан Обычный",
            resourceId = R.raw.kick
        ), AudioTrack(
            UUID.randomUUID().toString(),
            1.0f,
            1,
            1,
            0.5f,
            "Барабан classic",
            resourceId = R.raw.clasic_kick
        ), AudioTrack(
            UUID.randomUUID().toString(),
            1.0f,
            1,
            1,
            0.5f,
            "Барабан Новый",
            resourceId = R.raw.lowkik
        )
    )
    val tracksGloom = listOf(
        AudioTrack(
            UUID.randomUUID().toString(),
            0.5f,
            1,
            1,
            0.9f,
            "Sad music",
            resourceId = R.raw.longsad
        ), AudioTrack(
            UUID.randomUUID().toString(),
            0.5f,
            1,
            1,
            0.9f,
            "Very sad music",
            resourceId = R.raw.longsad
        ), AudioTrack(
            UUID.randomUUID().toString(),
            0.5f,
            1,
            1,
            0.9f,
            "Not sad music",
            resourceId = R.raw.longsad
        )
    )
    val kicks2 = listOf(
        AudioTrack(
            UUID.randomUUID().toString(),
            1.0f,
            1,
            1,
            0.5f,
            "Барабан 1",
            resourceId = R.raw.kick
        ), AudioTrack(
            UUID.randomUUID().toString(),
            1.0f,
            1,
            1,
            0.5f,
            "Барабан 2",
            resourceId = R.raw.clasic_kick
        ), AudioTrack(
            UUID.randomUUID().toString(),
            1.0f,
            1,
            1,
            0.5f,
            "Барабан 3",
            resourceId = R.raw.lowkik
        )
    )
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Sample(Icons.Default.Face, "gitara", kicks, expanded == "gitara", ::updateExpanded)
        Sample(Icons.Default.Build, "viol", tracksGloom, expanded == "viol", ::updateExpanded)
        Sample(Icons.Default.DateRange, "booms", kicks2, expanded == "booms", ::updateExpanded)
    }
}
