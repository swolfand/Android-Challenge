package com.swolfand.ticktock.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.text.format.DateFormat
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

        binding.chronometer.format = DateFormat.format("kk:mm:ss", 800L).toString()
        binding.chronometer.base = SystemClock.elapsedRealtime()
        binding.chronometer.start()
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

        if (countDownTimer == null) {
            createTimer(80000)

        } else {
            timerViewModel.getTime()?.let {
                createTimer(it.toMillis())
            }
        }
    }

    private fun setPausedViewState() {
        countDownTimer?.cancel()
        binding.playButton.hide()
        binding.pauseButton.hide()

        binding.resumeStop.show()
        binding.actionBarContent.show()

        binding.playPauseLabel.text = getString(R.string.resume_end)


    }

    private fun setStoppedViewState() {

    }


    fun createTimer(countDownAmount: Long) {
        countDownTimer = object : CountDownTimer(countDownAmount, 1000L) {
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

    fun currentTime(): Timer {
        val hour = if(binding.hour.text.isNullOrEmpty()) 0 else binding.hour.toInt()
        val min = if(binding.minutes.text.isNullOrEmpty()) 0 else binding.minutes.toInt()
        val seconds = if(binding.seconds.text.isNullOrEmpty()) 0 else binding.seconds.toInt()

        return Timer(hour,min,seconds)
    }
}

sealed class TimerState
object Running : TimerState()
object Stopped : TimerState()
object Paused : TimerState()
