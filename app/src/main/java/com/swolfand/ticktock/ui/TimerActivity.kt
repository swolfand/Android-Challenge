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

@AndroidEntryPoint
class TimerActivity : AppCompatActivity(), OnActivityFinishedListener {
    private val timerViewModel: TimerViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

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

        binding.currentActivityRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.nextActivityRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        setInitialState()

        timerHelper = TimerHelper(binding, this)

        compositeDisposable += timerViewModel
            .relay
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                currentActivities = it
                currentOrder = it.keys.minOrNull()!!
                onOrderChanged()
            }

        binding.apply {
            playButton.setOnClickListener { setRunningViewState() }
            pauseButton.setOnClickListener { setPausedViewState() }
            resumeButton.setOnClickListener { setRunningViewState() }
            stopButton.setOnClickListener { setStoppedViewState() }
            endButton.setOnClickListener { setEndViewState() }
        }

        timerViewModel.getActivities()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    //endregion

    override fun onActivityFinished() {
        countDownTimer?.cancel()
        countDownTimer = null
        if (hasNextActivity) {
            currentOrder += 1
            binding.seconds.text = currentModel.durationSeconds.toString()
            binding.millis.text = getString(R.string.zeros)
            onOrderChanged()
            resetState()
        } else {
            setStoppedViewState()
        }
    }

    private fun onOrderChanged() {
        binding.currentActivityRecycler.adapter = ActivityAdapter(currentActivities[currentOrder]!!)

        if (hasNextActivity) {
            binding.nextActivityRecycler.adapter =
                currentActivities[currentOrder + 1]?.let { ActivityAdapter(it) }
        }
    }

    // region view state callbacks

    private fun setRunningViewState() {
        binding.apply {
            playButton.hide()
            resumeStop.hide()
            banner.collapse()

            pauseButton.show()
            playPauseLabel.text = getString(R.string.pause_drill)
        }

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
        binding.apply {
            playButton.hide()
            pauseButton.hide()
            resumeStop.show()
            banner.expand()
            playPauseLabel.text = getString(R.string.resume_end)
        }
    }

    private fun setStoppedViewState() {
        binding.apply {
            endButton.show()
            playButton.hide()
            pauseButton.hide()
            resumeStop.hide()
            playPauseLabel.hide()
        }
    }

    private fun setEndViewState() {
        currentOrder = 0
        onOrderChanged()
        resetState()
        setInitialState()
    }

    private fun resetState() {
        binding.apply {
            endButton.hide()
            pauseButton.hide()
            stopButton.hide()
            playButton.show()
            playPauseLabel.text = getText(R.string.play_drill)
            banner.collapse(true)
        }
    }

    //endregion

    private fun setInitialState() {
        binding.seconds.text = currentModel.durationSeconds.toString()
        binding.millis.text = getString(R.string.zero_zero)
    }
}

interface OnActivityFinishedListener {
    fun onActivityFinished()
}
