package com.swolfand.ticktock.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.swolfand.ticktock.model.Activity
import com.swolfand.ticktock.model.Instructor
import com.swolfand.ticktock.model.Material
import com.swolfand.ticktock.persistence.dao.ActivityDao
import com.swolfand.ticktock.persistence.dao.InstructorDao
import com.swolfand.ticktock.persistence.dao.MaterialDao

@Database(
    entities = [
        Activity::class,
        Instructor::class,
        Material::class
    ], version = 2
)
abstract class TickTockDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao

    abstract fun instructorDao(): InstructorDao

    abstract fun materialDao(): MaterialDao

    companion object {

        @Volatile
        private var INSTANCE: TickTockDatabase? = null

        fun getDatabase(context: Context): TickTockDatabase {
            return INSTANCE ?: Room
                .databaseBuilder(
                    context.applicationContext,
                    TickTockDatabase::class.java,
                    "ticktock.db"
                )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigrationOnDowngrade()
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }

    }
}