package com.memoryhunter.goofer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddButton(onClick: () -> Unit = {}) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.padding(30.dp)
    ) {
        Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.add))
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
    currentMediaPlayer: MutableState<MediaPlayer?>,
    currentContext: Context
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(3)
        ) {
            items(soundList) { sound ->
                SoundButton(
                    sound,
                    currentMediaPlayer = currentMediaPlayer,
                    currentContext = currentContext
                )
            }
        }
    }
}

@Composable
fun SoundButton(
    sound: Sound,
    currentMediaPlayer: MutableState<MediaPlayer?>,
    currentContext: Context
) {
    Box(
        modifier = Modifier
            .clickable {
                playSound(currentMediaPlayer, currentContext, sound.uri)
            }
            .padding(8.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = sound.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(2.dp)
        )
    }
}

@Composable
fun AudioPopup(
    onDismissRequest: () -> Unit,
    onAddAudio: (Sound) -> Unit,
    currentMediaPlayer: MutableState<MediaPlayer?>,
    currentContext: Context
) {
    val name = remember { mutableStateOf("") }
    val uri = remember { mutableStateOf<Uri?>(null) }

    val fileChooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri.value = it }

    Dialog(onDismissRequest = {
        currentMediaPlayer.value?.release()
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
                    text = stringResource(id = R.string.add_popup_text),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text(stringResource(id = R.string.audio_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        fileChooserLauncher.launch("audio/*")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.select_file))
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
                            playSound(currentMediaPlayer, currentContext, uri.value!!)
                        },
                        enabled = uri.value != null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(id = R.string.play_test))
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
                        Text(stringResource(id = R.string.add))
                    }
                }
            }
        }
    }
}
