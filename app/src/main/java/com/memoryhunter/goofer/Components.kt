package com.memoryhunter.goofer

import android.media.MediaPlayer
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddButton(onClick: () -> Unit = {}) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.padding(30.dp)
    ) {
        Icon(Icons.Filled.Add, contentDescription = R.string.add.toString())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar(title: String) {
    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.headlineLarge) }
    )
}

@Composable
fun SoundboardSection(
    soundList: List<Sound>,
    currentMediaPlayer: MutableState<MediaPlayer?>
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(3)
        ) {
            items(soundList) { sound ->
                SoundButton(sound, currentMediaPlayer = currentMediaPlayer)
            }
        }
    }
}

@Composable
fun SoundButton(sound: Sound, currentMediaPlayer: MutableState<MediaPlayer?>) {
    Button(
        onClick = {
            currentMediaPlayer.value?.reset()
            currentMediaPlayer.value?.setDataSource(sound.uri.toString())
            currentMediaPlayer.value?.prepare()
            currentMediaPlayer.value?.start()
        },
        modifier = Modifier
            .size(150.dp, 20.dp)
            .padding(40.dp)
            .aspectRatio(1f)
    ) {
        Text(sound.name)
    }
}

@Composable
fun AudioPopup(
    onDismissRequest: () -> Unit,
    onAddAudio: (Sound) -> Unit,
    currentMediaPlayer: MutableState<MediaPlayer?>,
) {
    val name = remember { mutableStateOf("") }
    val uri = remember { mutableStateOf<Uri?>(null) }

    val fileChooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri.value = it }

    Dialog(onDismissRequest = {
        currentMediaPlayer.value?.release() // Release the MediaPlayer when the dialog is dismissed
        onDismissRequest()
    }) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = R.string.add_popup_text.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text(R.string.audio_name.toString()) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        fileChooserLauncher.launch("audio/*")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(R.string.select_file.toString())
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            playSound() /* TODO */
                        },
                        enabled = uri.value != null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(R.string.play_test.toString())
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        onClick = {
                            val sound = Sound(name.value, uri.value!!)
                            onAddAudio(sound)
                        },
                        enabled = uri.value != null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(R.string.add.toString())
                    }
                }
            }
        }
    }
}
