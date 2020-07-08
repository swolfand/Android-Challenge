package com.swolfand.ticktock.ui

import android.os.Parcelable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.versionedparcelable.VersionedParcelize
import com.swolfand.ticktock.model.Activity
import com.swolfand.ticktock.persistence.dao.ActivityDao
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize

private const val TIMER_STATE = "timer_state"
private const val MILLIS_IN_HOUR = 3600000
private const val MILLIS_IN_MINUTE = 60000
private const val MILLIS_IN_SECOND = 1000

class TimerViewModel @ViewModelInject constructor(
    private val activityDao: ActivityDao,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getActivities(): @NonNull Flowable<Map<Int, List<Activity>>> {

        return RxJavaBridge.toV3Flowable(activityDao.getActivities())
            .map { it.groupBy { activities -> activities.order } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveTimer(timer: Timer) {
        savedStateHandle.set(TIMER_STATE, timer)
    }

    fun getTime() = savedStateHandle.get<Timer>(TIMER_STATE)
}

@Parcelize
data class Timer(val hour: Int = 0, val minute: Int = 0, val second: Int = 0) : Parcelable

fun Timer.toMillis(): Long =
    ((this.hour * MILLIS_IN_HOUR) + (this.minute * MILLIS_IN_MINUTE) + (this.second * MILLIS_IN_SECOND)).toLong()

fun Long.toTimer(): Timer = Timer(
    (this / MILLIS_IN_HOUR).toInt(),
    (this / MILLIS_IN_MINUTE).toInt(),
    (this / MILLIS_IN_SECOND).toInt()
)
