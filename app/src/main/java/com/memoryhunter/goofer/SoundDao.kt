package com.memoryhunter.goofer

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SoundDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSound(sound: Sound)

    @Query("SELECT * FROM sound")
    fun readAllData(): LiveData<List<Sound>>

    @Update
    suspend fun updateSound(sound: Sound)

    @Delete
    suspend fun deleteSound(sound: Sound)

    @Query("DELETE FROM sound")
    suspend fun deleteAllSounds()

}