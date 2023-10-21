package com.anafthdev.imget.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anafthdev.imget.data.model.WImage

@Database(
    entities = [
        WImage::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun wImageDao(): WImageDao

    companion object {
        /**
         * AppDatabase instance
         */
        private var INSTANCE: AppDatabase? = null

        /**
         * Get AppDatabase instance
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(AppDatabase::class) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }.also { INSTANCE = it }
        }
    }

}