package com.memoryhunter.goofer.ui.elements

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.memoryhunter.goofer.R
import com.memoryhunter.goofer.database.SoundViewModel
import com.memoryhunter.goofer.objects.Sound
import com.memoryhunter.goofer.objects.playSound

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
    currentContext: Context,
    soundViewModel: SoundViewModel
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
                    currentContext = currentContext,
                    onRemoveAudio = {
                        soundViewModel.deleteSound(sound)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SoundButton(
    sound: Sound,
    currentMediaPlayer: MutableState<MediaPlayer?>,
    currentContext: Context,
    onRemoveAudio: () -> Unit
) {
    val showDropdown = rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    playSound(currentMediaPlayer, currentContext, sound.uri)
                },
                onLongClick = {
                    showDropdown.value = true
                }
            )
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
        if (showDropdown.value) {
            ManagerDropdownMenu(
                expanded = showDropdown.value,
                onDismissRequest = { showDropdown.value = false },
                onRemoveAudio = onRemoveAudio
            )
        }
    }
}

@Composable
fun PermissionPopup(
    onDismissRequest: () -> Unit,
    onClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.permission_popup_text),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.grant_permission))
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioPermissionCheck(
    showPermissionPopup: MutableState<Boolean>,
    audioMediaPermission: PermissionState
) {
    if (!audioMediaPermission.status.isGranted) {
        if (showPermissionPopup.value) {
            PermissionPopup(
                onDismissRequest = {
                    showPermissionPopup.value = false
                },
                onClick = {
                    audioMediaPermission.launchPermissionRequest()
                }
            )
        } else {
            showPermissionPopup.value = true
        }
    } else {
        showPermissionPopup.value = false
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

@Composable
fun ManagerDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onRemoveAudio: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
            .padding(8.dp)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete_entry)) },
                onClick = onRemoveAudio
            )
        }
    }
}