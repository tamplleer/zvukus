package com.example.zvukus.view.samples

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.screen.main.PlayerViewModel
import com.example.zvukus.model.AudioTrack

@Composable
fun Sample(
    icon: Int,
    description: String,
    tracks: List<AudioTrack>,
    expanded: Boolean,
    updateExpanded: (String) -> Unit,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val addTrack = playerViewModel::addTrack

    SampleUi(
        addTrack, icon,
        description,
        tracks,
        expanded,
        updateExpanded
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SampleUi(
    addTrack: (AudioTrack) -> Unit, icon: Int,
    description: String,
    tracks: List<AudioTrack>,
    expanded: Boolean,
    updateExpanded: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                role = Role.Button,
                onClick = {
                    tracks[0]?.let {
                        addTrack(it.copy())
                        updateExpanded("")
                    }
                },
                onLongClick = {
                    updateExpanded(description)
                }),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier

                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(30.dp)
                .animateContentSize()

        ) {
            Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = icon),
                    tint = MaterialTheme.colorScheme.background,
                    contentDescription = "add sample in layers"
                )
                if (expanded) {
                    tracks.map {
                        Button(onClick = {
                            addTrack(it.copy())
                            updateExpanded("")
                        }) {
                            Text(text = it.name)
                        }
                    }
                }
            }
        }
        if (!expanded) {
            Text(text = description)
        }

    }
}