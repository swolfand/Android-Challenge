package com.swolfand.ticktock.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.swolfand.ticktock.CountDownTimer
import com.swolfand.ticktock.R
import com.swolfand.ticktock.databinding.ActivityTimerBinding
import com.swolfand.ticktock.plusAssign
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

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
                currentOrder = it.keys.first()
                onOrderChanged(currentOrder)
            }

        compositeDisposable += timerState.subscribe {
            when (it) {
                Running -> setRunningViewState()
                Stopped -> setStoppedViewState()
                Paused -> setPausedViewState()
            }
        }

        binding.playButton.setOnClickListener { timerState.onNext(Running) }
        binding.pauseButton.setOnClickListener { timerState.onNext(Paused) }
        binding.resumeButton.setOnClickListener { timerState.onNext(Running) }
        binding.stopButton.setOnClickListener { timerState.onNext(Stopped) }

        timerViewModel.getActivities()
    }

    private fun onOrderChanged(currentOrder: Int) {

    }

    private fun setRunningViewState() {
        binding.playButton.hide()
        binding.resumeStop.hide()
        binding.banner.collapse()

        binding.pauseButton.show()
        binding.playPauseLabel.text = getString(R.string.pause_drill)

        if (countDownTimer == null) {
            countDownTimer = timerHelper.createTimer(80000)
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

        timerViewModel.timer = timerHelper.currentTime()
    }

    private fun setStoppedViewState() {

    }

    override fun onActivityFinished() {
        TODO("Not yet implemented")
    }

}

interface OnActivityFinishedListener {
    fun onActivityFinished()
}

sealed class TimerState
object Running : TimerState()
object Stopped : TimerState()
object Paused : TimerState()
