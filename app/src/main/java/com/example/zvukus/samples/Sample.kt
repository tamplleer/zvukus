package com.example.zvukus.samples

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zvukus.services.AudioTrack
import com.example.zvukus.PlayerViewModel

@OptIn(ExperimentalFoundationApi::class)
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
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier

                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(30.dp)
                .animateContentSize()
                .combinedClickable(
                    role = Role.Button,
                    onClick = {
                        Log.i("aa", "short")
                        tracks[0]?.let {
                            addTrack(it.copy())
                            updateExpanded("")
                        }

                    },
                    onLongClick = {
                        Log.i("aa", "logn")
                        updateExpanded(description)
                    })

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