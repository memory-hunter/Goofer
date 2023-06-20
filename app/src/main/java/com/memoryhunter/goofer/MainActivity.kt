package com.memoryhunter.goofer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.memoryhunter.goofer.database.SoundDatabase
import com.memoryhunter.goofer.database.SoundRepository
import com.memoryhunter.goofer.database.SoundViewModel
import com.memoryhunter.goofer.ui.elements.MainWindow
import com.memoryhunter.goofer.ui.theme.GooferTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val soundDao = SoundDatabase.getDatabase(applicationContext).soundDao()
        val soundRepository = SoundRepository.getInstance(soundDao)
        val soundViewModel: SoundViewModel by viewModels {
            SoundViewModel.provideFactory(soundRepository)
        }
        setContent {
            MainWindow(soundViewModel)
        }
    }
}

@Preview(showBackground = false)
@Composable
fun DefaultPreview() {
    GooferTheme {
        MainActivity()
    }
}
