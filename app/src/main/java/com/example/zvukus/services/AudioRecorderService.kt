package com.example.zvukus.services

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

interface AudioRecorder {
    fun start(fileName: String): File
    fun startRecordTrack(fileName: String): File
    fun stop()
}

class AudioRecorderService @Inject constructor(@ApplicationContext val context: Context) :
    AudioRecorder {
    private var mediaRecorder: MediaRecorder? = null
    private fun createRecorder(): MediaRecorder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(context)
        else
            MediaRecorder()


    override fun start(fileName: String): File {
        val file = File(context.cacheDir, fileName)
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(file).fd)
            prepare()
            start()
            mediaRecorder = this
        }
        return file
    }

    override fun startRecordTrack(fileName: String): File {
        val file = File(context.cacheDir, fileName)
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(file).fd)
            prepare()
            start()
            mediaRecorder = this
        }
        return file
    }

    override fun stop() {
        mediaRecorder?.stop()
        mediaRecorder?.reset()
        mediaRecorder = null
    }

}