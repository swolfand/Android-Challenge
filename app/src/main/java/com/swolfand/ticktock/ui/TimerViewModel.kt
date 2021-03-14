package com.swolfand.ticktock.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.swolfand.ticktock.persistence.dao.ActivityDao
import com.swolfand.ticktock.persistence.dao.InstructorDao
import com.swolfand.ticktock.persistence.dao.MaterialDao
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

private const val TIMER_STATE = "timer_state"
private const val CURRENT_ORDER = "current_order"


class TimerViewModel @ViewModelInject constructor(
    private val activityDao: ActivityDao,
    private val materialDao: MaterialDao,
    private val instructorDao: InstructorDao,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val relay: PublishSubject<Map<Int, List<TimerUiModel>>> = PublishSubject.create()

    var timer: Timer?
        get() = savedStateHandle.get(TIMER_STATE)
        set(value) = savedStateHandle.set(TIMER_STATE, value)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getActivities() {
        val disposable = activityDao.getActivities()
            .subscribeOn(Schedulers.io())
            .take(1)
            .flatMapIterable { it }
            .flatMap {
                materialDao.getMaterial(it.materialId).take(1).map { material ->
                    TimerUiModel(
                        durationSeconds = it.durationSeconds,
                        order = it.order,
                        activityName = material.name,
                        instructorId = it.instructorId
                    )
                }
            }
            .flatMap {
                instructorDao.getInstructor(it.instructorId).take(1).map { instructor ->
                    it.copy(instructorName = "${instructor.firstName} ${instructor.lastName}")
                }
            }
            .toList()
            .map { it.groupBy { model -> model.order }.toSortedMap() }
            .subscribe({
                relay.onNext(it)
            }, { Timber.e(it) })

        compositeDisposable.add(disposable)
    }
}

data class TimerUiModel(
    val durationSeconds: Int = -1,
    val order: Int = -1,
    val instructorName: String = "",
    val activityName: String = "",
    val instructorId: Int = -1
)