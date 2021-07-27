package cau.dururung.dururung.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(SleepData::class), version=1)
abstract class SleepDatabase : RoomDatabase() {
    abstract fun sleepDao(): SleepDataDao

    companion object {
        private var INSTANCE : SleepDatabase? = null

        fun getInstance(context: Context) : SleepDatabase? {
            if (INSTANCE == null) {
                synchronized(SleepDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        SleepDatabase::class.java, "sleep-db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}