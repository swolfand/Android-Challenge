package com.swolfand.ticktock.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.swolfand.ticktock.CountDownTimer
import com.swolfand.ticktock.R
import com.swolfand.ticktock.databinding.ActivityTimerBinding
import com.swolfand.ticktock.plusAssign
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

@AndroidEntryPoint
class TimerActivity : AppCompatActivity(), OnActivityFinishedListener {
    private val timerViewModel: TimerViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()
    private val timerState: PublishSubject<TimerState> = PublishSubject.create()

    private var countDownTimer: CountDownTimer? = null
    private var currentOrder: Int = 0

    private lateinit var timerHelper: TimerHelper
    private lateinit var binding: ActivityTimerBinding
    private lateinit var currentActivities: Map<Int, List<TimerUiModel>>

    private val hasNextActivity: Boolean by lazy { currentActivities[currentOrder + 1] != null }
    private val currentModel: TimerUiModel by lazy { currentActivities[currentOrder]?.first()!! }

    //region lifecycle callbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timerHelper = TimerHelper(binding, this)

        compositeDisposable += timerViewModel
            .relay
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                currentActivities = it
                currentOrder = it.keys.minOrNull()!!
                onOrderChanged(currentOrder)
            }

        compositeDisposable += timerState.subscribe({
            when (it) {
                Running -> setRunningViewState()
                Stopped -> setStoppedViewState()
                Paused -> setPausedViewState()
            }
        }, { Timber.e(it) })

        binding.playButton.setOnClickListener { timerState.onNext(Running) }
        binding.pauseButton.setOnClickListener { timerState.onNext(Paused) }
        binding.resumeButton.setOnClickListener { timerState.onNext(Running) }
        binding.stopButton.setOnClickListener { timerState.onNext(Stopped) }

        timerViewModel.getActivities()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    //endregion

    override fun onActivityFinished() {
        if (hasNextActivity) {
            countDownTimer = null
            binding.seconds.text = currentModel.durationSeconds.toString()
            binding.millis.text = "00"
            countDownTimer = timerHelper.createTimer(currentModel.durationSeconds.toLong())
            currentOrder += 1
            onOrderChanged()
        } else {
            setStoppedViewState()
        }
    }

    private fun onOrderChanged() {
        binding.currentActivityRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.currentActivityRecycler.adapter = ActivityAdapter(currentActivities[currentOrder]!!)

        binding.nextActivityRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        if (hasNextActivity) {
            binding.nextActivityRecycler.adapter =
                currentActivities[currentOrder + 1]?.let { ActivityAdapter(it) }
        }
    }

    // region view state callbacks

    private fun setRunningViewState() {
        binding.playButton.hide()
        binding.resumeStop.hide()
        binding.banner.collapse()

        binding.pauseButton.show()
        binding.playPauseLabel.text = getString(R.string.pause_drill)

        if (countDownTimer == null) {
            val currentTime = currentActivities[currentOrder]?.first()?.durationSeconds!!.toLong()
            countDownTimer =
                timerHelper.createTimer((currentTime * 1000))
            countDownTimer?.start()
        } else {
            countDownTimer?.resume()
        }
    }

    private fun setPausedViewState() {
        countDownTimer?.pause()
        binding.playButton.hide()
        binding.pauseButton.hide()

        binding.resumeStop.show()
        binding.banner.expand()

        binding.playPauseLabel.text = getString(R.string.resume_end)
    }

    private fun setStoppedViewState() {
        binding.endButton.show()
        binding.playButton.hide()
        binding.pauseButton.hide()
        binding.resumeStop.hide()
        binding.playPauseLabel.hide()
    }

    //endregion
}

interface OnActivityFinishedListener {
    fun onActivityFinished()
}

sealed class TimerState
object Running : TimerState()
object Stopped : TimerState()
object Paused : TimerState()
