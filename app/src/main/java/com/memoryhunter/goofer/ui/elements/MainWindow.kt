package com.memoryhunter.goofer.ui.elements

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.memoryhunter.goofer.R
import com.memoryhunter.goofer.database.SoundViewModel
import com.memoryhunter.goofer.ui.theme.GooferTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainWindow(soundViewModel: SoundViewModel) {

    val currentContext = LocalContext.current
    val showPopup = remember { mutableStateOf(false) }
    val currentMediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }
    val soundList by soundViewModel.soundList.observeAsState(emptyList())
    val showPermissionPopup = remember { mutableStateOf(true) }
    val audioMediaPermission = rememberPermissionState(android.Manifest.permission.READ_MEDIA_AUDIO)

    GooferTheme {
        Column {
            TitleBar(title = stringResource(id = R.string.app_name))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
            ) {
                SoundboardSection(
                    soundList = soundList,
                    currentMediaPlayer = currentMediaPlayer,
                    currentContext = currentContext,
                    soundViewModel = soundViewModel
                )
                AddButton(onClick = {
                    showPopup.value = true
                })
            }
            if (showPopup.value) {
                AudioPopup(onDismissRequest = {
                    showPopup.value = false
                }, onAddAudio = {
                    soundViewModel.addSound(it)
                    showPopup.value = false
                }, currentMediaPlayer = currentMediaPlayer, currentContext = currentContext
                )
            }
        }
        AudioPermissionCheck(
            audioMediaPermission = audioMediaPermission,
            showPermissionPopup = showPermissionPopup
        )
    }
}
