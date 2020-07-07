package com.swolfand.ticktock.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.swolfand.ticktock.model.Activity
import com.swolfand.ticktock.persistence.dao.ActivityDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val TIMER_STATE = "timer_state"

class TimerViewModel @ViewModelInject constructor(
    private val activityDao: ActivityDao,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getActivities(): @NonNull Flowable<Map<Int, List<Activity>>> {
        return activityDao.getActivities()
            .map { it.groupBy { activities -> activities.order } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveTimer(timer: Timer) {
        savedStateHandle.set(TIMER_STATE, timer)
    }

    fun getTime() = savedStateHandle.get<Timer>(TIMER_STATE)
}

data class Timer(val hour: Int = 0, val minute: Int = 0, val second: Int = 0)