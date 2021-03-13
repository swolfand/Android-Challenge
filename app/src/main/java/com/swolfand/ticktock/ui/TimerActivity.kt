package com.swolfand.ticktock.ui

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.swolfand.ticktock.R
import com.swolfand.ticktock.databinding.ActivityTimerBinding
import com.swolfand.ticktock.databinding.ItemActivityBinding
import com.swolfand.ticktock.plusAssign
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

@AndroidEntryPoint
class TimerActivity : AppCompatActivity() {
    private val timerViewModel: TimerViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var binding: ActivityTimerBinding
    private val relay: PublishSubject<Timer> = PublishSubject.create()
    private val timerState: PublishSubject<TimerState> = PublishSubject.create()
    private var countDownTimer: CountDownTimer? = null
    private lateinit var timerDelegate: TimerDelegate
    private lateinit var itemActivityBinding: ItemActivityBinding
    private lateinit var activityMap: MutableMap<Int, List<TimerUiModel>>

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

        binding.playButton.setOnClickListener { timerState.onNext(Running) }
        binding.pauseButton.setOnClickListener { timerState.onNext(Paused) }
        binding.resumeButton.setOnClickListener { timerState.onNext(Running) }
        binding.stopButton.setOnClickListener { timerState.onNext(Stopped) }
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable += timerViewModel.getActivities()
            .subscribe({ map ->
                activityMap = map.toMutableMap()
                activityMap.entries.forEachIndexed { index, entry ->
                    if (index == 0) {
                        entry.value.forEach {
                            val view = layoutInflater.inflate(
                                R.layout.item_activity,
                                binding.currentActivityContainer
                            )
                            itemActivityBinding = ItemActivityBinding.bind(view)
                            itemActivityBinding.activityPlayer.text = it.instructorName
                            itemActivityBinding.activityTitle.text = it.activityName
                            binding.currentActivityContainer.addView(view)
                        }
                    } else if (index == 1) {

                    }
                }
            }, { })
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

        timerViewModel.timer = timerDelegate.currentTime()
    }

    private fun setStoppedViewState() {

    }

}

sealed class TimerState
object Running : TimerState()
object Stopped : TimerState()
object Paused : TimerState()
