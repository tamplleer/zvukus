package com.example.zvukus.model

import com.example.zvukus.TrackMediaPlayer
import java.io.File
import java.util.UUID
import javax.inject.Inject

data class AudioTrack @Inject constructor(
    val id: String,
    var volume: Float,
    var intervalTime: Float? = null,
    val name: String,
    var mediaPlayer: TrackMediaPlayer? = null,
    val resourceId: Int? = null,
    val file: File? = null,
    var soundId: Int = -1,
    var mute: Boolean = false,
) {
    companion object {
        fun defaultTrack(): AudioTrack =
            AudioTrack(UUID.randomUUID().toString(), 1.0f, 2f, "Unknown")
    }

}