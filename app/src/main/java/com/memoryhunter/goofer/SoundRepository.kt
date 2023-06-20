package com.memoryhunter.goofer

import androidx.lifecycle.LiveData

class SoundRepository(private val soundDao: SoundDao) {

    val readAllData: LiveData<List<Sound>> = soundDao.readAllData()

    suspend fun addSound(sound: Sound) {
        soundDao.addSound(sound)
    }

    suspend fun updateSound(sound: Sound) {
        soundDao.updateSound(sound)
    }

    suspend fun deleteSound(sound: Sound) {
        soundDao.deleteSound(sound)
    }

    suspend fun deleteAllSounds() {
        soundDao.deleteAllSounds()
    }

}