package com.swolfand.ticktock

import android.app.Application
import com.swolfand.ticktock.model.Activity
import com.swolfand.ticktock.persistence.TickTockDatabase
import com.swolfand.ticktock.persistence.dao.ActivityDao
import com.swolfand.ticktock.persistence.dao.InstructorDao
import com.swolfand.ticktock.persistence.dao.MaterialDao
import com.swolfand.ticktock.seed.activities
import com.swolfand.ticktock.seed.instructors
import com.swolfand.ticktock.seed.materials
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TickTockApplication : Application() {

    @Inject
    lateinit var materialDao: MaterialDao

    @Inject
    lateinit var activityDao: ActivityDao

    @Inject
    lateinit var instructorDao: InstructorDao

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        seedDb()
    }

    private fun seedDb() {
        materialDao.insertAll(materials)
        activityDao.insertAll(activities)
        instructorDao.insertAll(instructors)
    }
}
