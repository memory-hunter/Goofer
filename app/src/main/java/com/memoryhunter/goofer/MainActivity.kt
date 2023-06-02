package com.memoryhunter.goofer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.memoryhunter.goofer.ui.theme.GooferTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainWindow()
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
