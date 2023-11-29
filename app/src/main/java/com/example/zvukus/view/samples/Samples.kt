package com.example.zvukus.view.samples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.zvukus.R
import com.example.zvukus.services.AudioTrack
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
            2f,
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
            "Voice",
            resourceId = R.raw.voice
        ), AudioTrack(
            UUID.randomUUID().toString(),
            0.5f,
            1,
            1,
            0.9f,
            "Not sad music",
            resourceId = R.raw.no_sad
        )
    )
    val good = listOf(
        AudioTrack(
            UUID.randomUUID().toString(),
            1.0f,
            1,
            1,
            0.5f,
            "Shakers",
            resourceId = R.raw.shakers
        ), AudioTrack(
            UUID.randomUUID().toString(),
            1.0f,
            1,
            1,
            0.5f,
            "Pitch",
            resourceId = R.raw.pitch
        ), AudioTrack(
            UUID.randomUUID().toString(),
            1.0f,
            1,
            1,
            0.5f,
            "Sci",
            resourceId = R.raw.sci
        )
    )
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Sample(R.drawable.boom, "Booms!", kicks, expanded == "Booms!", ::updateExpanded)
        Sample(
            R.drawable.sad_man,
            "Sad long",
            tracksGloom,
            expanded == "Sad long",
            ::updateExpanded
        )
        Sample(R.drawable.poc, "Some good", good, expanded == "Some good", ::updateExpanded)
    }
}
