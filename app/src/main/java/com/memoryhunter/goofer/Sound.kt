package com.memoryhunter.goofer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.room.Entity

@Entity(tableName = "sound", primaryKeys = ["name"])
data class Sound(val name: String, val uri: Uri)

fun playSound(
    currentMediaPlayer: MutableState<MediaPlayer?>,
    currentContext: Context,
    uri: Uri
) {
    currentMediaPlayer.value = MediaPlayer.create(currentContext, uri)
    currentMediaPlayer.value?.start()
    currentMediaPlayer.value?.setOnCompletionListener {
        currentMediaPlayer.value?.release()
    }
}