package com.swolfand.ticktock.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.ReplayRelay
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

        binding.playButton.setOnClickListener {
            startTimer()
        }

        timerState.subscribe {
            when (it) {
                Running -> setRunningViewState()
                Stopped -> setStoppedViewState()
                Paused -> setPausedViewState()
            }
        }
    }

    private fun setPausedViewState() {

    }

    private fun setStoppedViewState() {
// TODO
    }

    private fun setRunningViewState() {
// TODO
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable += timerViewModel.getActivities()
            .subscribe {

            }
    }

    fun startTimer() {
        object : CountDownTimer(5000, 1000L) {
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {
                val timer = millisUntilFinished.toTimer()
                if (timer.hour != 0) {
                    binding.hour.visibility = View.VISIBLE
                    binding.hour.text = timer.hour.toString()
                } else {
                    binding.hour.visibility = View.GONE
                    binding.separator.visibility = View.GONE
                }

                if (timer.minute != 0) {
                    binding.minutes.visibility = View.VISIBLE
                    binding.minutes.text = timer.minute.toString()
                } else {
                    binding.minutes.visibility = View.GONE
                    binding.separator2.visibility = View.GONE
                }

                if (timer.second != 0) {
                    binding.seconds.visibility = View.VISIBLE
                    binding.seconds.text = timer.second.toString()
                } else {
                    binding.seconds.visibility = View.GONE
                }
            }

        }.start()
    }
}

sealed class TimerState
object Running : TimerState()
object Stopped : TimerState()
object Paused : TimerState()
