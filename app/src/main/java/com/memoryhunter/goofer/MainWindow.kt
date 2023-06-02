package com.memoryhunter.goofer

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.memoryhunter.goofer.ui.theme.GooferTheme

@Composable
fun MainWindow() {
    val showPopup = remember { mutableStateOf(false) }
    val soundList = remember { mutableStateListOf<Sound>() }

    val currentMediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }
    val currentContext = LocalContext.current

    GooferTheme {
        Column {
            TitleBar(title = stringResource(id = R.string.app_name))
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                SoundboardSection(soundList, currentMediaPlayer, currentContext)
                AddButton(
                    onClick = {
                        showPopup.value = true
                    }
                )
            }
            if (showPopup.value) {
                AudioPopup(
                    onDismissRequest = {
                        showPopup.value = false
                    },
                    onAddAudio = { sound ->
                        soundList.add(sound)
                        showPopup.value = false
                    },
                    currentMediaPlayer = currentMediaPlayer,
                    currentContext = currentContext
                )
            }
        }
    }
}
