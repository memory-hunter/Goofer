package com.memoryhunter.goofer.database

import androidx.lifecycle.LiveData
import com.memoryhunter.goofer.objects.Sound

class SoundRepository(private val soundDao: SoundDao) {

    fun readAllData(): LiveData<List<Sound>> {
        return soundDao.readAllData()
    }

    suspend fun addSound(sound: Sound) {
        soundDao.addSound(sound)
    }

    suspend fun deleteSound(sound: Sound) {
        soundDao.deleteSound(sound)
    }

    suspend fun deleteAllSounds() {
        soundDao.deleteAllSounds()
    }

    companion object {
        @Volatile
        private var instance: SoundRepository? = null

        fun getInstance(soundDao: SoundDao): SoundRepository {
            return instance ?: synchronized(this) {
                instance ?: SoundRepository(soundDao).also { instance = it }
            }
        }
    }
}