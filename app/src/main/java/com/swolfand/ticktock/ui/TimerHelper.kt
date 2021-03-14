package com.swolfand.ticktock.ui

import com.swolfand.ticktock.CountDownTimer
import com.swolfand.ticktock.databinding.ActivityTimerBinding

class TimerHelper(
    private val binding: ActivityTimerBinding,
    private val listener: OnActivityFinishedListener?
) {

    fun createTimer(countDownAmount: Long): CountDownTimer {
        return object : CountDownTimer(countDownAmount, 10) {
            override fun onFinish() {
                listener?.onActivityFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                with(binding) {
                    seconds.apply {
                        show()
                        text = (millisUntilFinished / 1000).toString()
                    }

                    millis.apply {
                        show()
                        text = (millisUntilFinished % 1000).toString()
                    }
                }
            }

        }.start()
    }
}