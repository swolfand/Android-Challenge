package com.swolfand.ticktock.ui

import com.swolfand.ticktock.CountDownTimer
import com.swolfand.ticktock.databinding.ActivityTimerBinding

class TimerHelper(
    private val binding: ActivityTimerBinding,
    private val listener: OnActivityFinishedListener?
) {

    fun createTimer(countDownAmount: Long): CountDownTimer {
        return object : CountDownTimer(countDownAmount, 1000L) {
            override fun onFinish() {
                listener?.onActivityFinished()
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
        val hour = if (binding.hour.text.isNullOrEmpty()) 0 else binding.hour.toInt()
        val min = if (binding.minutes.text.isNullOrEmpty()) 0 else binding.minutes.toInt()
        val seconds = if (binding.seconds.text.isNullOrEmpty()) 0 else binding.seconds.toInt()

        return Timer(hour, min, seconds)
    }
}