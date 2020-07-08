package com.swolfand.ticktock.ui

import android.os.Parcelable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.swolfand.ticktock.model.Activity
import com.swolfand.ticktock.persistence.dao.ActivityDao
import com.swolfand.ticktock.persistence.dao.InstructorDao
import com.swolfand.ticktock.persistence.dao.MaterialDao
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize

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

    var currentOrder: Int?
        get() = savedStateHandle.get<Int>(CURRENT_ORDER)
        set(value) = savedStateHandle.set(CURRENT_ORDER, value)

    var timer: Timer?
        get() = savedStateHandle.get(TIMER_STATE)
        set(value) = savedStateHandle.set(TIMER_STATE, value)

    fun getActivities(): @NonNull Single<Map<Int, List<TimerUiModel>>>? {
        return RxJavaBridge.toV3Flowable(activityDao.getActivities())
            .flatMapIterable { it }
            .flatMap {
                materialDao.getMaterial(it.materialId).map { material ->
                    TimerUiModel(
                        durationSeconds = it.durationSeconds,
                        order = it.order,
                        materialName = material.name,
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
    val materialName: String = "",
    val instructorId: Int = -1
)

fun Timer.toMillis(): Long =
    ((this.hour * MILLIS_IN_HOUR) + (this.minute * MILLIS_IN_MINUTE) + (this.second * MILLIS_IN_SECOND)).toLong()

fun Long.toTimer(): Timer = Timer(
    (this / MILLIS_IN_HOUR).toInt(),
    (this / MILLIS_IN_MINUTE).toInt(),
    (this / MILLIS_IN_SECOND).toInt()
)
