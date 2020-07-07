package com.enes.weather.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [City::class],
    version = 1
)
abstract class CityDatabase : RoomDatabase() {
    abstract val cityDao: CityDao

    companion object {
        @Volatile
        private var INSTANCE: CityDatabase? = null

        fun getInstance(context: Context): CityDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CityDatabase::class.java,
                        "city_name_db"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}