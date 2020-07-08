package com.swolfand.ticktock.ui

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.ReplayRelay
import com.swolfand.ticktock.R
import com.swolfand.ticktock.databinding.ActivityTimerBinding
import com.swolfand.ticktock.plusAssign
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class TimerActivity : AppCompatActivity() {
    private val timerViewModel: TimerViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var binding: ActivityTimerBinding
    private val relay: ReplayRelay<Timer> = ReplayRelay.createWithSize(1)
    private val timerState: BehaviorRelay<TimerState> = BehaviorRelay.create()
    private var countDownTimer: CountDownTimer? = null
    private lateinit var timerDelegate: TimerDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        timerDelegate = TimerDelegate(binding)
        val view = binding.root
        setContentView(view)

        timerState.subscribe {
            when (it) {
                Running -> setRunningViewState()
                Stopped -> setStoppedViewState()
                Paused -> setPausedViewState()
            }
        }

        binding.playButton.setOnClickListener { timerState.accept(Running) }
        binding.pauseButton.setOnClickListener { timerState.accept(Paused) }
        binding.resumeButton.setOnClickListener { timerState.accept(Running) }
        binding.stopButton.setOnClickListener { timerState.accept(Stopped) }
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable += timerViewModel.getActivities()
            .subscribe {
            }
    }

    private fun setRunningViewState() {
        binding.playButton.hide()
        binding.resumeStop.hide()
        binding.banner.collapse()

        binding.pauseButton.show()
        binding.playPauseLabel.text = getString(R.string.pause_drill)

        if (countDownTimer == null) {
            countDownTimer = timerDelegate.createTimer(80000)
        } else {
            countDownTimer?.start()
        }
    }

    private fun setPausedViewState() {
        countDownTimer?.cancel()
        binding.playButton.hide()
        binding.pauseButton.hide()

        binding.resumeStop.show()
        binding.banner.expand()

        binding.playPauseLabel.text = getString(R.string.resume_end)

        timerViewModel.saveTimer(timerDelegate.currentTime())
    }

    private fun setStoppedViewState() {

    }

}

sealed class TimerState
object Running : TimerState()
object Stopped : TimerState()
object Paused : TimerState()
