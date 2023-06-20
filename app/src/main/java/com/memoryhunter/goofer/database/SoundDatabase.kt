package com.memoryhunter.goofer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.memoryhunter.goofer.objects.Converters
import com.memoryhunter.goofer.objects.Sound

@Database(entities = [Sound::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SoundDatabase : RoomDatabase() {

    abstract fun soundDao(): SoundDao

    companion object {
        @Volatile
        private var currentInstance: SoundDatabase? = null

        fun getDatabase(currentContext: Context): SoundDatabase {
            val tempInstance = currentInstance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    currentContext.applicationContext,
                    SoundDatabase::class.java,
                    "sound_database"
                ).build()
                currentInstance = instance
                return instance
            }
        }
    }
}