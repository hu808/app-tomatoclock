package com.hhshs3440.potatoclock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.fabPause
import kotlinx.android.synthetic.main.activity_main.fabResume
import kotlinx.android.synthetic.main.activity_main.fabStart
import kotlinx.android.synthetic.main.activity_main.fabStop
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.time
import kotlinx.android.synthetic.main.activity_timer.*

class timer : AppCompatActivity() {

    private var isPaused = false
    private var isCancelled = false
    private var resumeFromMillis: Long = 0
    var denominator: Int = 0
    var counter: Int = 0
    var numberoftomato: Int = 0
    var post_id: String = ""
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        val millisInFutureforwork: Long = 4000
        val millisInFutureforrest: Long = 2000
        val countDownInterval: Long = 1000
        progressBar.setProgress(100)
        fabPause.hide()
        fabResume.hide()
        post_id = intent.getStringExtra("編號")
        intent = Intent(this, plan::class.java)
        chef.visibility = View.INVISIBLE

        givenumoftomato.setOnClickListener {
            numberoftomato = ratingBar.rating.toInt() * 2 - 1
            txvnumoftomato.text = "你輸入的蛋蛋數:"+ratingBar.rating.toInt()
        }

        fabStart.setOnClickListener {
            if (txvnumoftomato.text == "") {
                Toast.makeText(this, "請先輸入蛋蛋數", Toast.LENGTH_SHORT).show()
            } else {
                if (counter % 2 == 0) {
                    denominator = millisInFutureforwork.toInt()
                    timer(
                        millisInFutureforwork,
                        countDownInterval,
                        denominator,
                        numberoftomato
                    ).start()
                } else {
                    denominator = millisInFutureforrest.toInt()
                    timer(
                        millisInFutureforrest,
                        countDownInterval,
                        denominator,
                        numberoftomato
                    ).start()
                }
                it.isEnabled = false
                fabStop.isEnabled = true
                fabPause.isEnabled = true
                fabStart.hide()
                fabPause.show()
                fabResume.hide()
                fabStop.show()

                isCancelled = false
                isPaused = false
                chef.visibility = View.VISIBLE
            }

        }
        fabStop.setOnClickListener {
            // Start the timer
            isCancelled = true
            isPaused = false

            it.isEnabled = false
            fabStart.isEnabled = true
            fabPause.isEnabled = false
            fabResume.isEnabled = false
            fabResume.hide()
            fabStart.show()
            time.setText("25:00")
            time.textSize = 40F
            chef.visibility = View.INVISIBLE


        }
        // Count down timer pause button
        fabPause.setOnClickListener {
            isPaused = true
            isCancelled = false
            it.isEnabled = false
            fabStart.isEnabled = false
            fabStop.isEnabled = true
            fabResume.isEnabled = true
            fabPause.hide()
            fabResume.show()
            chef.visibility = View.INVISIBLE

        }
        fabResume.setOnClickListener {
            // Resume the timer
            timer(resumeFromMillis, countDownInterval, denominator, numberoftomato).start()

            isPaused = false
            isCancelled = false

            it.isEnabled = false
            fabPause.isEnabled = true
            fabStart.isEnabled = false
            fabStop.isEnabled = true
            fabStart.hide()
            fabResume.hide()
            fabPause.show()
            chef.visibility = View.VISIBLE

        }

    }

    private fun timer(
        millisInFuture: Long,
        countDownInterval: Long,
        denominator: Int,
        numberoftomato: Int
    ): CountDownTimer {

        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {

                progressBar.setProgress(millisUntilFinished.toInt() / (denominator / 100))
                var minutesRemaining = millisUntilFinished / 60000
                var secondsRemaining = millisUntilFinished / 1000 - minutesRemaining * 60
                if (isPaused) {

                    if (minutesRemaining < 10)
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()
                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()
                    else
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()
                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()
                    resumeFromMillis = millisUntilFinished
                    cancel()
                } else if (isCancelled) {
                    if (minutesRemaining < 10)
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()
                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()
                    else
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()
                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()
                    cancel()
                } else {
                    if (minutesRemaining < 10)
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()
                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()
                    else
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()
                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()
                }
            }

            override fun onFinish() {
                if (counter % 2 == 0) {
                    time.text = "休息五分鐘"

                } else {
                    time.text = "開工囉"


                }
                fabPause.hide()
                fabResume.hide()
                fabStop.hide()
                fabStart.show()
                fabStart.isEnabled = true
                counter++
                progressBar.setProgress(0)
                chef.visibility = View.INVISIBLE

                if (counter == numberoftomato) {
                    db.collection("WorkDay").document(post_id).update("finished", "是")
                    fabStart.hide()
                    fabStop.hide()
                    fabPause.hide()
                    fabResume.hide()
                    time.text = "大功告成"
                    chef.visibility = View.INVISIBLE

                }
            }
        }

    }

}