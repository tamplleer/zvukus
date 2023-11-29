package com.example.zvukus.services

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

interface SharedService {
    fun shared(file: File?)
}

class SharedAudio @Inject constructor(@ApplicationContext val context: Context) : SharedService {
    override fun shared(file: File?) {
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file!!
        )
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "audio/*"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        sendIntent.flags =Intent.FLAG_ACTIVITY_NEW_TASK
        val shareIntent = Intent.createChooser(sendIntent, "Share Sound File").apply {
            flags=Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(shareIntent)
    }

}