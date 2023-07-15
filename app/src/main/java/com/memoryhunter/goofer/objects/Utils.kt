package com.memoryhunter.goofer.objects

import android.content.Context
import android.net.Uri

class Utils {
    companion object {
        fun writeFile(
            currentContext: Context,
            fileName: String,
            content: ByteArray
        ) {
            val outputStream = currentContext.openFileOutput(fileName, Context.MODE_PRIVATE)
            outputStream.write(content)
            outputStream.close()
        }

        fun readFile(
            currentContext: Context,
            fileName: String
        ): Uri {
            return Uri.parse(currentContext.filesDir.toString() + "/" + fileName)
        }

        fun readAudioFromUri(
            currentContext: Context,
            uri: Uri
        ): ByteArray {
            val inputStream = uri.let { currentContext.contentResolver.openInputStream(it) }
            val bytes = ByteArray(inputStream!!.available())
            inputStream.read(bytes, 0, bytes.size)
            return bytes
        }
    }
}