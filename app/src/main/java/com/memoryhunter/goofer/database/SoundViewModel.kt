package com.memoryhunter.goofer.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.memoryhunter.goofer.objects.Sound
import kotlinx.coroutines.launch

class SoundViewModel(private val repository: SoundRepository) : ViewModel() {

    val soundList: LiveData<List<Sound>> = repository.readAllData()

    fun addSound(sound: Sound) {
        viewModelScope.launch {
            repository.addSound(sound)
        }
    }

    fun deleteSound(sound: Sound) {
        viewModelScope.launch {
            repository.deleteSound(sound)
        }
    }

    fun deleteAllSounds() {
        viewModelScope.launch {
            repository.deleteAllSounds()
        }
        /* TOUSE */
    }

    companion object {
        fun provideFactory(repository: SoundRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(SoundViewModel::class.java)) {
                        return SoundViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
