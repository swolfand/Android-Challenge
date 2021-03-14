package com.swolfand.ticktock.ui

import android.os.Parcelable
import com.swolfand.ticktock.CountDownTimer
import com.swolfand.ticktock.databinding.ActivityTimerBinding
import kotlinx.parcelize.Parcelize

private const val MILLIS_IN_HOUR = 3600000
private const val MILLIS_IN_MINUTE = 60000
private const val MILLIS_IN_SECOND = 1000

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

@Parcelize
data class Timer(val second: Int = 0, val millis: Int = 0) : Parcelable

fun Long.toTimer(): Timer = Timer(
    (this / MILLIS_IN_SECOND).toInt()
)