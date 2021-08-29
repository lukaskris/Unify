package id.co.app.components.utils


/**
 * Created by Lukas Kristianto on 28/08/21 22.53.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

import android.os.CountDownTimer

abstract class CountDownTimerExt(var mMillisInFuture: Long, var mInterval: Long) {

    private lateinit var countDownTimer: CountDownTimer
    private var remainingTime: Long = 0
    private var isTimerPaused: Boolean = true

    init {
        this.remainingTime = mMillisInFuture
    }

    @Synchronized
    fun start() {
        if (isTimerPaused) {
            countDownTimer = object : CountDownTimer(remainingTime, mInterval) {
                override fun onFinish() {
                    onTimerFinish()
                    restart()
                }

                override fun onTick(millisUntilFinished: Long) {
                    remainingTime = millisUntilFinished
                    onTimerTick(millisUntilFinished)
                }

            }.apply {
                start()
            }
            isTimerPaused = false
        }
    }

    fun pause() {
        if (!isTimerPaused) {
            countDownTimer.cancel()
        }
        isTimerPaused = true
    }

    fun restart() {
        if(this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
            remainingTime = mMillisInFuture
            isTimerPaused = true
        } else {
            start()
        }
    }

    fun dispose(){
        if(this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

    abstract fun onTimerTick(millisUntilFinished: Long)
    abstract fun onTimerFinish()

}