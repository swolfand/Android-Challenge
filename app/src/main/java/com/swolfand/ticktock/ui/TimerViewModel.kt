package com.swolfand.ticktock.ui

import android.os.Parcelable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.swolfand.ticktock.persistence.dao.ActivityDao
import com.swolfand.ticktock.persistence.dao.InstructorDao
import com.swolfand.ticktock.persistence.dao.MaterialDao
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.parcelize.Parcelize

private const val TIMER_STATE = "timer_state"
private const val CURRENT_ORDER = "current_order"
private const val MILLIS_IN_HOUR = 3600000
private const val MILLIS_IN_MINUTE = 60000
private const val MILLIS_IN_SECOND = 1000

class TimerViewModel @ViewModelInject constructor(
    private val activityDao: ActivityDao,
    private val materialDao: MaterialDao,
    private val instructorDao: InstructorDao,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var timer: Timer?
        get() = savedStateHandle.get(TIMER_STATE)
        set(value) = savedStateHandle.set(TIMER_STATE, value)

    fun getActivities(): Single<Map<Int, List<TimerUiModel>>> {
        return activityDao.getActivities()
            .flatMapIterable { it }
            .flatMap {
                materialDao.getMaterial(it.materialId).map { material ->
                    TimerUiModel(
                        durationSeconds = it.durationSeconds,
                        order = it.order,
                        activityName = material.name,
                        instructorId = it.instructorId
                    )
                }
            }
            .flatMap {
                instructorDao.getInstructor(it.instructorId).map { instructor ->
                    it.copy(instructorName = instructor.givenName + instructor.familyName)
                }
            }
            .toList()
            .map { it.groupBy { activities -> activities.order } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

@Parcelize
data class Timer(val hour: Int = 0, val minute: Int = 0, val second: Int = 0) : Parcelable

data class TimerUiModel(
    val durationSeconds: Int = -1,
    val order: Int = -1,
    val instructorName: String = "",
    val activityName: String = "",
    val instructorId: Int = -1
)

fun Timer.toMillis(): Long =
    ((this.hour * MILLIS_IN_HOUR) + (this.minute * MILLIS_IN_MINUTE) + (this.second * MILLIS_IN_SECOND)).toLong()

fun Long.toTimer(): Timer = Timer(
    (this / MILLIS_IN_HOUR).toInt(),
    (this / MILLIS_IN_MINUTE).toInt(),
    (this / MILLIS_IN_SECOND).toInt()
)
