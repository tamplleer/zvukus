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
import com.example.zvukus.model.AudioTrack
import java.util.UUID


data class SamplesGroup(
    val icon: Int,
    val description: String,
    val tracks: List<AudioTrack>,
) {
    fun expanded(id: String) = description == id

}

@Composable
fun Samples(modifier: Modifier) {
    val kicks = listOf(
        AudioTrack(
            UUID.randomUUID().toString(),
            0.8f,
            1f,
            "Барабан Обычный",
            resourceId = R.raw.kick
        ), AudioTrack(
            UUID.randomUUID().toString(),
            0.8f,
            1f,
            "Барабан classic",
            resourceId = R.raw.clasic_kick
        ), AudioTrack(
            UUID.randomUUID().toString(),
            0.8f,
            1f,
            "Барабан Новый",
            resourceId = R.raw.lowkik
        )
    )
    val tracksGloom = listOf(
        AudioTrack(
            UUID.randomUUID().toString(),
            0.8f,
            1f,
            "Sad music",
            resourceId = R.raw.longsad
        ), AudioTrack(
            UUID.randomUUID().toString(),
            0.8f,
            1f,
            "Voice",
            resourceId = R.raw.voice
        ), AudioTrack(
            UUID.randomUUID().toString(),
            0.8f,
            1f,
            "Not sad music",
            resourceId = R.raw.no_sad
        )
    )
    val good = listOf(
        AudioTrack(
            UUID.randomUUID().toString(),
            0.8f,
            1f,
            "Shakers",
            resourceId = R.raw.shakers
        ), AudioTrack(
            UUID.randomUUID().toString(),
            0.8f,
            1f,
            "Pitch",
            resourceId = R.raw.pitch
        ), AudioTrack(
            UUID.randomUUID().toString(),
            0.8f,
            1f,
            "Sci",
            resourceId = R.raw.sci
        )
    )


    val listSampleGroup = listOf(
        SamplesGroup(R.drawable.boom, "Booms!", kicks),
        SamplesGroup(R.drawable.sad_man, "Sad long", tracksGloom),
        SamplesGroup(R.drawable.poc, "Some good", good)
    )
    SamplesUi(modifier = modifier, listSampleGroup)
}

@Composable
fun SamplesUi(modifier: Modifier, listSampleGroup: List<SamplesGroup>) {
    var expanded by remember { mutableStateOf("") }
    fun updateExpanded(id: String) {
        expanded = id
    }

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        listSampleGroup.map {
            Sample(it.icon, it.description, it.tracks, it.expanded(expanded), ::updateExpanded)
        }
    }

}
