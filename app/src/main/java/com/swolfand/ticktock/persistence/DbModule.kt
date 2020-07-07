package com.swolfand.ticktock.persistence

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DbModule {

    @Provides
    fun providesContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideTickTockDatabase(context: Context) = TickTockDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideActivityDao(tickTockDatabase: TickTockDatabase) = tickTockDatabase.activityDao()

    @Provides
    @Singleton
    fun provideMaterialDao(tickTockDatabase: TickTockDatabase) = tickTockDatabase.materialDao()

    @Provides
    @Singleton
    fun provideInstructorDao(tickTockDatabase: TickTockDatabase) = tickTockDatabase.instructorDao()
}