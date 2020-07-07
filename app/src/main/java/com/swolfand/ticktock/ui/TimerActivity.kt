package com.swolfand.ticktock.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
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
        binding.actionBarContent.hide()

        binding.pauseButton.show()
        binding.playPauseLabel.text = getString(R.string.pause_drill)
    }

    private fun setPausedViewState() {
        binding.playButton.hide()
        binding.pauseButton.hide()

        binding.resumeStop.show()
        binding.actionBarContent.show()

        binding.playPauseLabel.text = getString(R.string.resume_end)
    }

    private fun setStoppedViewState() {

    }


    fun startTimer() {
        object : CountDownTimer(5000, 1000L) {
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {
                val timer = millisUntilFinished.toTimer()
                if (timer.hour != 0) {
                    binding.hour.show()
                    binding.hour.text = timer.hour.toString()
                } else {
                    binding.hour.hide()
                    binding.separator.hide()
                }

                if (timer.minute != 0) {
                    binding.minutes.show()
                    binding.minutes.text = timer.minute.toString()
                } else {
                    binding.minutes.hide()
                    binding.separator2.hide()
                }

                if (timer.second != 0) {
                    binding.seconds.show()
                    binding.seconds.text = timer.second.toString()
                } else {
                    binding.seconds.hide()
                }
            }

        }.start()
    }
}

sealed class TimerState
object Running : TimerState()
object Stopped : TimerState()
object Paused : TimerState()
