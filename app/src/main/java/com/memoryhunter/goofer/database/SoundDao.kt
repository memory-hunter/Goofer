package com.memoryhunter.goofer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.memoryhunter.goofer.objects.Sound

@Dao
interface SoundDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSound(sound: Sound)

    @Query("SELECT * FROM sound")
    fun readAllData(): LiveData<List<Sound>>

    @Delete
    suspend fun deleteSound(sound: Sound)

    @Query("DELETE FROM sound")
    suspend fun deleteAllSounds()

}